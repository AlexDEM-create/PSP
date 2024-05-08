package com.flacko.payment.method.impl;

import com.flacko.bank.service.BankService;
import com.flacko.common.currency.Currency;
import com.flacko.common.exception.BankNotFoundException;
import com.flacko.common.exception.TerminalNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.id.IdGenerator;
import com.flacko.payment.method.service.PaymentMethod;
import com.flacko.payment.method.service.PaymentMethodBuilder;
import com.flacko.payment.method.service.PaymentMethodType;
import com.flacko.payment.method.service.exception.PaymentMethodInvalidBankCardNumberException;
import com.flacko.payment.method.service.exception.PaymentMethodInvalidPhoneNumberException;
import com.flacko.payment.method.service.exception.PaymentMethodMissingRequiredAttributeException;
import com.flacko.terminal.service.TerminalService;
import com.flacko.trader.team.service.TraderTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class PaymentMethodBuilderImpl implements InitializablePaymentMethodBuilder {

    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("^\\d{16}$");
    private static final Pattern PHONE_NUMBER_PATTERN =
            Pattern.compile("^(^8|7|\\+7)((\\d{10})|(\\s\\(\\d{3}\\)\\s\\d{3}\\s\\d{2}\\s\\d{2}))$");

    private final Instant now = Instant.now();

    private final PaymentMethodRepository paymentMethodRepository;
    private final BankService bankService;
    private final TraderTeamService traderTeamService;
    private final TerminalService terminalService;

    private PaymentMethodPojo.PaymentMethodPojoBuilder pojoBuilder;

    @Override
    public PaymentMethodBuilder initializeNew() {
        pojoBuilder = PaymentMethodPojo.builder()
                .id(new IdGenerator().generateId());
        return this;
    }

    @Override
    public PaymentMethodBuilder initializeExisting(PaymentMethod existingPaymentMethod) {
        pojoBuilder = PaymentMethodPojo.builder()
                .primaryKey(existingPaymentMethod.getPrimaryKey())
                .id(existingPaymentMethod.getId())
                .type(existingPaymentMethod.getType())
                .number(existingPaymentMethod.getNumber())
                .holderName(existingPaymentMethod.getHolderName())
                .currency(existingPaymentMethod.getCurrency())
                .bankId(existingPaymentMethod.getBankId())
                .traderTeamId(existingPaymentMethod.getTraderTeamId())
                .busy(existingPaymentMethod.isBusy())
                .createdDate(existingPaymentMethod.getCreatedDate())
                .updatedDate(now)
                .deletedDate(existingPaymentMethod.getDeletedDate().orElse(null));
        return this;
    }

    @Override
    public PaymentMethodBuilder withType(PaymentMethodType type) {
        pojoBuilder.type(type);
        return this;
    }

    @Override
    public PaymentMethodBuilder withNumber(String number) {
        pojoBuilder.number(number);
        return this;
    }

    @Override
    public PaymentMethodBuilder withHolderName(String holderName) {
        pojoBuilder.holderName(holderName);
        return this;
    }

    @Override
    public PaymentMethodBuilder withCurrency(Currency currency) {
        pojoBuilder.currency(currency);
        return this;
    }

    @Override
    public PaymentMethodBuilder withBankId(String id) {
        pojoBuilder.bankId(id);
        return this;
    }

    @Override
    public PaymentMethodBuilder withTraderTeamId(String traderTeamId) {
        pojoBuilder.traderTeamId(traderTeamId);
        return this;
    }

    @Override
    public PaymentMethodBuilder withTerminalId(String terminalId) {
        pojoBuilder.terminalId(terminalId);
        return this;
    }

    @Override
    public PaymentMethodBuilder withBusy(boolean busy) {
        pojoBuilder.busy(busy);
        return this;
    }

    @Override
    public PaymentMethodBuilder withArchived() {
        pojoBuilder.deletedDate(now);
        return this;
    }

    @Override
    public PaymentMethod build() throws PaymentMethodMissingRequiredAttributeException, TraderTeamNotFoundException,
            PaymentMethodInvalidBankCardNumberException, BankNotFoundException, TerminalNotFoundException,
            PaymentMethodInvalidPhoneNumberException {
        PaymentMethodPojo card = pojoBuilder.build();
        validate(card);
        paymentMethodRepository.save(card);
        return card;
    }

    private void validate(PaymentMethodPojo pojo) throws PaymentMethodMissingRequiredAttributeException,
            BankNotFoundException, TraderTeamNotFoundException, PaymentMethodInvalidBankCardNumberException,
            TerminalNotFoundException, PaymentMethodInvalidPhoneNumberException {
        if (pojo.getId() == null || pojo.getId().isBlank()) {
            throw new PaymentMethodMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getType() == null) {
            throw new PaymentMethodMissingRequiredAttributeException("type", Optional.of(pojo.getId()));
        }
        if (pojo.getNumber() == null || pojo.getNumber().isBlank()) {
            throw new PaymentMethodMissingRequiredAttributeException("number", Optional.of(pojo.getId()));
        }
        if (pojo.getType() == PaymentMethodType.BANK_CARD &&
                !CARD_NUMBER_PATTERN.matcher(pojo.getNumber()).matches()) {
            throw new PaymentMethodInvalidBankCardNumberException(pojo.getId(), pojo.getNumber());
        }
        if (pojo.getType() == PaymentMethodType.PHONE_NUMBER &&
                !PHONE_NUMBER_PATTERN.matcher(pojo.getNumber()).matches()) {
            throw new PaymentMethodInvalidPhoneNumberException(pojo.getId(), pojo.getNumber());
        }
        if (pojo.getHolderName() == null || pojo.getHolderName().isBlank()) {
            throw new PaymentMethodMissingRequiredAttributeException("holderName", Optional.of(pojo.getId()));
        }
        if (pojo.getCurrency() == null) {
            throw new PaymentMethodMissingRequiredAttributeException("currency", Optional.of(pojo.getId()));
        }
        if (pojo.getBankId() == null || pojo.getBankId().isBlank()) {
            throw new PaymentMethodMissingRequiredAttributeException("bankId", Optional.of(pojo.getId()));
        } else {
            bankService.get(pojo.getBankId());
        }
        if (pojo.getTraderTeamId() == null || pojo.getTraderTeamId().isBlank()) {
            throw new PaymentMethodMissingRequiredAttributeException("traderTeamId", Optional.of(pojo.getId()));
        } else {
            traderTeamService.get(pojo.getTraderTeamId());
        }
        if (pojo.getTerminalId() == null || pojo.getTerminalId().isBlank()) {
            throw new PaymentMethodMissingRequiredAttributeException("terminalId", Optional.of(pojo.getId()));
        } else {
            terminalService.get(pojo.getTerminalId());
        }
    }

}
