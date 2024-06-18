package com.flacko.payment.impl.outgoing;

import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;
import com.flacko.common.exception.MerchantInsufficientOutgoingBalanceException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.NoEligibleTraderTeamsException;
import com.flacko.common.exception.OutgoingPaymentIllegalStateTransitionException;
import com.flacko.common.exception.OutgoingPaymentInvalidAmountException;
import com.flacko.common.exception.OutgoingPaymentMissingRequiredAttributeException;
import com.flacko.common.exception.PaymentMethodNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.exception.UserNotFoundException;
import com.flacko.common.id.IdGenerator;
import com.flacko.common.operation.CrudOperation;
import com.flacko.common.payment.RecipientPaymentMethodType;
import com.flacko.common.state.PaymentState;
import com.flacko.merchant.service.Merchant;
import com.flacko.merchant.service.MerchantService;
import com.flacko.payment.method.service.PaymentMethodService;
import com.flacko.payment.service.outgoing.OutgoingPayment;
import com.flacko.payment.service.outgoing.OutgoingPaymentBuilder;
import com.flacko.trader.team.service.TraderTeam;
import com.flacko.trader.team.service.TraderTeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
@Slf4j
public class OutgoingPaymentBuilderImpl implements InitializableOutgoingPaymentBuilder {

    private static final Set<PaymentState> STATES_TO_NOTIFY = Set.of(PaymentState.VERIFIED);

    private final Instant now = Instant.now();

    private final OutgoingPaymentRepository outgoingPaymentRepository;
    private final MerchantService merchantService;
    private final TraderTeamService traderTeamService;
    private final PaymentMethodService paymentMethodService;
    private final RestTemplate restTemplate;

    private OutgoingPaymentPojo.OutgoingPaymentPojoBuilder pojoBuilder;
    private CrudOperation crudOperation;
    private String id;
    private PaymentState currentState;
    private String login;
    private String merchantId;

    @Override
    public OutgoingPaymentBuilder initializeNew(String login) {
        crudOperation = CrudOperation.CREATE;
        this.login = login;
        id = new IdGenerator().generateId();
        currentState = PaymentState.INITIATED;
        pojoBuilder = OutgoingPaymentPojo.builder()
                .id(id)
                .currentState(currentState);
        return this;
    }

    @Override
    public OutgoingPaymentBuilder initializeExisting(OutgoingPayment existingOutgoingPayment) {
        crudOperation = CrudOperation.UPDATE;
        pojoBuilder = OutgoingPaymentPojo.builder()
                .primaryKey(existingOutgoingPayment.getPrimaryKey())
                .id(existingOutgoingPayment.getId())
                .merchantId(existingOutgoingPayment.getMerchantId())
                .traderTeamId(existingOutgoingPayment.getTraderTeamId())
                .paymentMethodId(existingOutgoingPayment.getPaymentMethodId().orElse(null))
                .amount(existingOutgoingPayment.getAmount())
                .currency(existingOutgoingPayment.getCurrency())
                .recipient(existingOutgoingPayment.getRecipient())
                .bank(existingOutgoingPayment.getBank())
                .recipientPaymentMethodType(existingOutgoingPayment.getRecipientPaymentMethodType())
                .partnerPaymentId(existingOutgoingPayment.getPartnerPaymentId().orElse(null))
                .currentState(existingOutgoingPayment.getCurrentState())
                .createdDate(existingOutgoingPayment.getCreatedDate())
                .updatedDate(now);
        id = existingOutgoingPayment.getId();
        currentState = existingOutgoingPayment.getCurrentState();
        merchantId = existingOutgoingPayment.getMerchantId();
        return this;
    }

    @Override
    public OutgoingPaymentBuilder withRandomTraderTeamId(Optional<String> currentTraderTeamId)
            throws NoEligibleTraderTeamsException, TraderTeamNotFoundException {
        TraderTeam randomTraderTeam =
                traderTeamService.getRandomEligibleTraderTeamForOutgoingPayment(currentTraderTeamId);
        pojoBuilder.traderTeamId(randomTraderTeam.getId());
        return this;
    }

    @Override
    public OutgoingPaymentBuilder withTraderTeamId(String traderTeamId) {
        pojoBuilder.traderTeamId(traderTeamId);
        return this;
    }

    @Override
    public OutgoingPaymentBuilder withMerchantId(String merchantId) {
        this.merchantId = merchantId;
        pojoBuilder.merchantId(merchantId);
        return this;
    }

    @Override
    public OutgoingPaymentBuilder withPaymentMethodId(String paymentMethodId) {
        pojoBuilder.paymentMethodId(paymentMethodId);
        return this;
    }

    @Override
    public OutgoingPaymentBuilder withAmount(BigDecimal amount) {
        pojoBuilder.amount(amount);
        return this;
    }

    @Override
    public OutgoingPaymentBuilder withCurrency(Currency currency) {
        pojoBuilder.currency(currency);
        return this;
    }

    @Override
    public OutgoingPaymentBuilder withRecipient(String recipient) {
        pojoBuilder.recipient(recipient);
        return this;
    }

    @Override
    public OutgoingPaymentBuilder withBank(Bank bank) {
        pojoBuilder.bank(bank);
        return this;
    }

    @Override
    public OutgoingPaymentBuilder withRecipientPaymentMethodType(RecipientPaymentMethodType recipientPaymentMethodType) {
        pojoBuilder.recipientPaymentMethodType(recipientPaymentMethodType);
        return this;
    }

    @Override
    public OutgoingPaymentBuilder withPartnerPaymentId(String partnerPaymentId) {
        pojoBuilder.partnerPaymentId(partnerPaymentId);
        return this;
    }

    @Override
    public OutgoingPaymentBuilder withState(PaymentState newState)
            throws OutgoingPaymentIllegalStateTransitionException {
        if (!currentState.canChangeTo(newState)) {
            throw new OutgoingPaymentIllegalStateTransitionException(id, currentState, newState);
        }
        pojoBuilder.currentState(newState);
        return this;
    }

    @Override
    public OutgoingPayment build() throws OutgoingPaymentMissingRequiredAttributeException,
            TraderTeamNotFoundException, PaymentMethodNotFoundException, OutgoingPaymentInvalidAmountException,
            MerchantNotFoundException, MerchantInsufficientOutgoingBalanceException, UserNotFoundException {
        if (merchantId == null) {
            String merchantId = merchantService.getMy(login)
                    .getId();
            pojoBuilder.merchantId(merchantId);
        }

        OutgoingPaymentPojo outgoingPayment = pojoBuilder.build();
        validate(outgoingPayment);
        outgoingPaymentRepository.save(outgoingPayment);

        notifyMerchant(outgoingPayment);

        return outgoingPayment;
    }

    private void validate(OutgoingPaymentPojo pojo) throws OutgoingPaymentMissingRequiredAttributeException,
            MerchantNotFoundException, TraderTeamNotFoundException, PaymentMethodNotFoundException,
            OutgoingPaymentInvalidAmountException, MerchantInsufficientOutgoingBalanceException {
        if (pojo.getId() == null || pojo.getId().isBlank()) {
            throw new OutgoingPaymentMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getMerchantId() == null || pojo.getMerchantId().isBlank()) {
            throw new OutgoingPaymentMissingRequiredAttributeException("merchantId", Optional.of(pojo.getId()));
        } else {
            Merchant merchant = merchantService.get(pojo.getMerchantId());
            if (crudOperation == CrudOperation.CREATE && merchant.isOutgoingTrafficStopped()) {
                throw new MerchantInsufficientOutgoingBalanceException(merchant.getId(), merchant.getCountry());
            }
        }
        if (pojo.getTraderTeamId() == null || pojo.getTraderTeamId().isBlank()) {
            throw new OutgoingPaymentMissingRequiredAttributeException("traderTeamId", Optional.of(pojo.getId()));
        } else {
            traderTeamService.get(pojo.getTraderTeamId());
        }
        if (pojo.getCurrentState() == PaymentState.VERIFIED && pojo.getPaymentMethodId().isEmpty()) {
            throw new OutgoingPaymentMissingRequiredAttributeException("paymentMethodId", Optional.of(pojo.getId()));
        } else if (pojo.getPaymentMethodId().isPresent()) {
            paymentMethodService.get(pojo.getPaymentMethodId().get());
        }
        if (pojo.getAmount() == null) {
            throw new OutgoingPaymentMissingRequiredAttributeException("amount", Optional.of(pojo.getId()));
        } else if (pojo.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new OutgoingPaymentInvalidAmountException(pojo.getId());
        }
        if (pojo.getCurrency() == null) {
            throw new OutgoingPaymentMissingRequiredAttributeException("currency", Optional.of(pojo.getId()));
        }
        if (pojo.getRecipient() == null || pojo.getRecipient().isBlank()) {
            throw new OutgoingPaymentMissingRequiredAttributeException("recipient", Optional.of(pojo.getId()));
        }
        if (pojo.getBank() == null) {
            throw new OutgoingPaymentMissingRequiredAttributeException("bank", Optional.of(pojo.getId()));
        }
        if (pojo.getRecipientPaymentMethodType() == null) {
            throw new OutgoingPaymentMissingRequiredAttributeException("recipientPaymentMethodType",
                    Optional.of(pojo.getId()));
        }
        if (pojo.getCurrentState() == null) {
            throw new OutgoingPaymentMissingRequiredAttributeException("currentState", Optional.of(pojo.getId()));
        }
    }

    private void notifyMerchant(OutgoingPayment outgoingPayment) throws MerchantNotFoundException {
        Merchant merchant = merchantService.get(outgoingPayment.getMerchantId());
        if (merchant.getWebhook().isPresent() && STATES_TO_NOTIFY.contains(outgoingPayment.getCurrentState())) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String jsonPayload = String.format("{\"id\":\"%s\",\"state\":\"%s\"}", outgoingPayment.getId(),
                    outgoingPayment.getCurrentState());

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);

            try {
                restTemplate.postForEntity(merchant.getWebhook().get().toString(), requestEntity, String.class);
            } catch (Exception e) {
                log.error("Failed to notify merchant {} via webhook {} about outgoing payment {} state change {}",
                        outgoingPayment.getMerchantId(), merchant.getWebhook(), outgoingPayment.getId(),
                        outgoingPayment.getCurrentState(), e);
            }
        }
    }

}
