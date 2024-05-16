package com.flacko.payment.verification.receipt.impl;

import com.flacko.common.exception.OutgoingPaymentNotFoundException;
import com.flacko.common.id.IdGenerator;
import com.flacko.payment.service.outgoing.OutgoingPaymentService;
import com.flacko.payment.verification.receipt.service.ReceiptPaymentVerification;
import com.flacko.payment.verification.receipt.service.ReceiptPaymentVerificationBuilder;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationMissingRequiredAttributeException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationUnexpectedAmountException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class ReceiptPaymentVerificationBuilderImpl implements InitializableReceiptPaymentVerificationBuilder {

    private static final Pattern LAST_FOUR_DIGITS_PATTERN = Pattern.compile("^\\d{4}$");

    private final ReceiptPaymentVerificationRepository receiptPaymentVerificationRepository;
    private final OutgoingPaymentService outgoingPaymentService;

    private ReceiptPaymentVerificationPojo.ReceiptPaymentVerificationPojoBuilder pojoBuilder;

    @Override
    public ReceiptPaymentVerificationBuilder initializeNew() {
        pojoBuilder = ReceiptPaymentVerificationPojo.builder()
                .id(new IdGenerator().generateId());
        return this;
    }

    @Override
    public ReceiptPaymentVerificationBuilder withOutgoingPaymentId(String outgoingPaymentId) {
        pojoBuilder.outgoingPaymentId(outgoingPaymentId);
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

    @Transactional
    @Override
    public ReceiptPaymentVerification build() throws ReceiptPaymentVerificationMissingRequiredAttributeException,
            ReceiptPaymentVerificationUnexpectedAmountException, OutgoingPaymentNotFoundException {
        ReceiptPaymentVerificationPojo payment = pojoBuilder.build();
        validate(payment);
        receiptPaymentVerificationRepository.save(payment);
        return payment;
    }

    private void validate(ReceiptPaymentVerificationPojo pojo)
            throws ReceiptPaymentVerificationMissingRequiredAttributeException, OutgoingPaymentNotFoundException {
        if (pojo.getId() == null || pojo.getId().isBlank()) {
            throw new ReceiptPaymentVerificationMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getOutgoingPaymentId() == null || pojo.getOutgoingPaymentId().isBlank()) {
            throw new ReceiptPaymentVerificationMissingRequiredAttributeException("outgoingPaymentId",
                    Optional.of(pojo.getId()));
        }
        outgoingPaymentService.get(pojo.getOutgoingPaymentId());
        if (pojo.getData() == null) {
            throw new ReceiptPaymentVerificationMissingRequiredAttributeException("data", Optional.of(pojo.getId()));
        }
        if (pojo.getUploadedFile() == null) {
            throw new ReceiptPaymentVerificationMissingRequiredAttributeException("uploadedFile",
                    Optional.of(pojo.getId()));
        }
    }

}
