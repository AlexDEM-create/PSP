package com.flacko.payment.method.impl;

import com.flacko.common.bank.Bank;
import com.flacko.common.currency.Currency;
import com.flacko.common.exception.TerminalNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.id.IdGenerator;
import com.flacko.common.operation.CrudOperation;
import com.flacko.payment.method.service.PaymentMethod;
import com.flacko.payment.method.service.PaymentMethodBuilder;
import com.flacko.payment.method.service.exception.PaymentMethodInvalidBankCardNumberException;
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
    private final TraderTeamService traderTeamService;
    private final TerminalService terminalService;

    private PaymentMethodPojo.PaymentMethodPojoBuilder pojoBuilder;
    private CrudOperation crudOperation;

    @Override
    public PaymentMethodBuilder initializeNew() {
        crudOperation = CrudOperation.CREATE;
        pojoBuilder = PaymentMethodPojo.builder()
                .id(new IdGenerator().generateId())
                .enabled(false)
                .busy(false);
        return this;
    }

    @Override
    public PaymentMethodBuilder initializeExisting(PaymentMethod existingPaymentMethod) {
        crudOperation = CrudOperation.UPDATE;
        pojoBuilder = PaymentMethodPojo.builder()
                .primaryKey(existingPaymentMethod.getPrimaryKey())
                .id(existingPaymentMethod.getId())
                .number(existingPaymentMethod.getNumber())
                .firstName(existingPaymentMethod.getFirstName())
                .lastName(existingPaymentMethod.getLastName())
                .currency(existingPaymentMethod.getCurrency())
                .bank(existingPaymentMethod.getBank())
                .traderTeamId(existingPaymentMethod.getTraderTeamId())
                .terminalId(existingPaymentMethod.getTerminalId().orElse(null))
                .enabled(existingPaymentMethod.isEnabled())
                .busy(existingPaymentMethod.isBusy())
                .createdDate(existingPaymentMethod.getCreatedDate())
                .updatedDate(now)
                .deletedDate(existingPaymentMethod.getDeletedDate().orElse(null));
        return this;
    }

    @Override
    public PaymentMethodBuilder withNumber(String number) {
        pojoBuilder.number(number);
        return this;
    }

    @Override
    public PaymentMethodBuilder withFirstName(String firstName) {
        pojoBuilder.firstName(firstName);
        return this;
    }

    @Override
    public PaymentMethodBuilder withLastName(String lastName) {
        pojoBuilder.lastName(lastName);
        return this;
    }

    @Override
    public PaymentMethodBuilder withCurrency(Currency currency) {
        pojoBuilder.currency(currency);
        return this;
    }

    @Override
    public PaymentMethodBuilder withBank(Bank bank) {
        pojoBuilder.bank(bank);
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
    public PaymentMethodBuilder withEnabled(boolean enabled) {
        pojoBuilder.enabled(enabled);
        return this;
    }

    @Override
    public PaymentMethodBuilder withBusy(boolean busy) {
        pojoBuilder.busy(busy);
        return this;
    }

    @Override
    public PaymentMethodBuilder withArchived() {
        crudOperation = CrudOperation.DELETE;
        pojoBuilder.deletedDate(now);
        return this;
    }

    @Override
    public PaymentMethod build() throws PaymentMethodMissingRequiredAttributeException, TraderTeamNotFoundException,
            PaymentMethodInvalidBankCardNumberException, TerminalNotFoundException {
        PaymentMethodPojo card = pojoBuilder.build();
        validate(card);
        paymentMethodRepository.save(card);
        return card;
    }

    private void validate(PaymentMethodPojo pojo) throws PaymentMethodMissingRequiredAttributeException,
            TraderTeamNotFoundException, PaymentMethodInvalidBankCardNumberException, TerminalNotFoundException {
        if (pojo.getId() == null || pojo.getId().isBlank()) {
            throw new PaymentMethodMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getNumber() == null || pojo.getNumber().isBlank()) {
            throw new PaymentMethodMissingRequiredAttributeException("number", Optional.of(pojo.getId()));
        }
        if (!CARD_NUMBER_PATTERN.matcher(pojo.getNumber()).matches()) {
            throw new PaymentMethodInvalidBankCardNumberException(pojo.getId(), pojo.getNumber());
        }
        if (pojo.getFirstName() == null || pojo.getFirstName().isBlank()) {
            throw new PaymentMethodMissingRequiredAttributeException("firstName", Optional.of(pojo.getId()));
        }
        if (pojo.getLastName() == null || pojo.getLastName().isBlank()) {
            throw new PaymentMethodMissingRequiredAttributeException("lastName", Optional.of(pojo.getId()));
        }
        if (pojo.getCurrency() == null) {
            throw new PaymentMethodMissingRequiredAttributeException("currency", Optional.of(pojo.getId()));
        }
        if (pojo.getBank() == null) {
            throw new PaymentMethodMissingRequiredAttributeException("bank", Optional.of(pojo.getId()));
        }
        if (pojo.getTraderTeamId() == null || pojo.getTraderTeamId().isBlank()) {
            throw new PaymentMethodMissingRequiredAttributeException("traderTeamId", Optional.of(pojo.getId()));
        } else {
            traderTeamService.get(pojo.getTraderTeamId());
        }
        if (pojo.getTerminalId().isPresent()) {
            terminalService.get(pojo.getTerminalId().get());
        }
    }

}
