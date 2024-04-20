package com.flacko.payment.verification.receipt;

import com.flacko.auth.id.IdGenerator;
import com.flacko.payment.verification.receipt.exception.ReceiptPaymentVerificationMissingRequiredAttributeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;
import java.util.Map;
import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class ReceiptPaymentVerificationBuilderImpl implements InitializableReceiptPaymentVerificationBuilder {

    private final ReceiptPaymentVerificationRepository receiptPaymentVerificationRepository;

    private ReceiptPaymentVerificationPojo.ReceiptPaymentVerificationPojoBuilder pojoBuilder;

    @Override
    public ReceiptPaymentVerificationBuilder initializeNew() {
        pojoBuilder = ReceiptPaymentVerificationPojo.builder()
                .id(new IdGenerator().generateId());
        return this;
    }

    @Override
    public ReceiptPaymentVerificationBuilder withPaymentId(String paymentId) {
        pojoBuilder.paymentId(paymentId);
        return this;
    }

    @Override
    public ReceiptPaymentVerificationBuilder withRecipientFullName(String recipientFullName) {
        pojoBuilder.recipientFullName(recipientFullName);
        return this;
    }

    @Override
    public ReceiptPaymentVerificationBuilder withRecipientCardLastFourDigits(String recipientCardLastFourDigits) {
        pojoBuilder.recipientCardLastFourDigits(recipientCardLastFourDigits);
        return this;
    }

    @Override
    public ReceiptPaymentVerificationBuilder withSenderFullName(String senderFullName) {
        pojoBuilder.senderFullName(senderFullName);
        return this;
    }

    @Override
    public ReceiptPaymentVerificationBuilder withSenderCardLastFourDigits(String senderCardLastFourDigits) {
        pojoBuilder.senderCardLastFourDigits(senderCardLastFourDigits);
        return this;
    }

    @Override
    public ReceiptPaymentVerificationBuilder withAmount(BigDecimal amount) {
        pojoBuilder.amount(amount);
        return this;
    }

    @Override
    public ReceiptPaymentVerificationBuilder withAmountCurrency(Currency amountCurrency) {
        pojoBuilder.amountCurrency(amountCurrency);
        return this;
    }

    @Override
    public ReceiptPaymentVerificationBuilder withCommission(BigDecimal commission) {
        pojoBuilder.commission(commission);
        return this;
    }

    @Override
    public ReceiptPaymentVerificationBuilder withCommissionCurrency(Currency commissionCurrency) {
        pojoBuilder.commissionCurrency(commissionCurrency);
        return this;
    }

    @Override
    public ReceiptPaymentVerificationBuilder withData(Map<String, Object> data) {
        pojoBuilder.data(Collections.unmodifiableMap(data));
        return this;
    }

    @Override
    public ReceiptPaymentVerificationBuilder withUploadedFile(byte[] uploadedFile) {
        pojoBuilder.uploadedFile(uploadedFile);
        return this;
    }

    @Override
    public ReceiptPaymentVerification build() throws ReceiptPaymentVerificationMissingRequiredAttributeException {
        ReceiptPaymentVerificationPojo payment = pojoBuilder.build();
        validate(payment);
        receiptPaymentVerificationRepository.save(payment);
        return payment;
    }

    private void validate(ReceiptPaymentVerificationPojo pojo) throws ReceiptPaymentVerificationMissingRequiredAttributeException {
        if (pojo.getId() == null || pojo.getId().isEmpty()) {
            throw new ReceiptPaymentVerificationMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getPaymentId() == null || pojo.getPaymentId().isEmpty()) {
            throw new ReceiptPaymentVerificationMissingRequiredAttributeException("paymentId", Optional.of(pojo.getId()));
        }
        if (pojo.getRecipientFullName() == null || pojo.getRecipientFullName().isEmpty()) {
            throw new ReceiptPaymentVerificationMissingRequiredAttributeException("recipientFullName", Optional.of(pojo.getId()));
        }
        // validate card number
        if (pojo.getRecipientCardLastFourDigits() == null || pojo.getRecipientCardLastFourDigits().isEmpty()) {
            throw new ReceiptPaymentVerificationMissingRequiredAttributeException("recipientCardLastFourDigits", Optional.of(pojo.getId()));
        }
        if (pojo.getSenderFullName() == null || pojo.getSenderFullName().isEmpty()) {
            throw new ReceiptPaymentVerificationMissingRequiredAttributeException("senderFullName", Optional.of(pojo.getId()));
        }
        // validate card number
        if (pojo.getSenderCardLastFourDigits() == null || pojo.getSenderCardLastFourDigits().isEmpty()) {
            throw new ReceiptPaymentVerificationMissingRequiredAttributeException("senderCardLastFourDigits", Optional.of(pojo.getId()));
        }
        if (pojo.getAmount() == null) {
            throw new ReceiptPaymentVerificationMissingRequiredAttributeException("amount", Optional.of(pojo.getId()));
        }
        if (pojo.getAmountCurrency() == null) {
            throw new ReceiptPaymentVerificationMissingRequiredAttributeException("amountCurrency", Optional.of(pojo.getId()));
        }
        if (pojo.getCommission() == null) {
            throw new ReceiptPaymentVerificationMissingRequiredAttributeException("commission", Optional.of(pojo.getId()));
        }
        if (pojo.getCommissionCurrency() == null) {
            throw new ReceiptPaymentVerificationMissingRequiredAttributeException("commissionCurrency", Optional.of(pojo.getId()));
        }
        if (pojo.getData() == null || pojo.getData().isEmpty()) {
            throw new ReceiptPaymentVerificationMissingRequiredAttributeException("data", Optional.of(pojo.getId()));
        }
        if (pojo.getUploadedFile() == null) {
            throw new ReceiptPaymentVerificationMissingRequiredAttributeException("uploadedFile", Optional.of(pojo.getId()));
        }
    }


}

