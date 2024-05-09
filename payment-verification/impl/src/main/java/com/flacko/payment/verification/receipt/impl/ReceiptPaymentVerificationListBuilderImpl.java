package com.flacko.payment.verification.receipt.impl;

import com.flacko.payment.verification.receipt.service.ReceiptPaymentVerification;
import com.flacko.payment.verification.receipt.service.ReceiptPaymentVerificationListBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class ReceiptPaymentVerificationListBuilderImpl implements ReceiptPaymentVerificationListBuilder {

    private final ReceiptPaymentVerificationRepository receiptPaymentVerificationRepository;

    private Optional<String> outgoingPaymentId = Optional.empty();

    @Override
    public ReceiptPaymentVerificationListBuilder withOutgoingPaymentId(String outgoingPaymentId) {
        this.outgoingPaymentId = Optional.ofNullable(outgoingPaymentId);
        return this;
    }

    @Override
    public List<ReceiptPaymentVerification> build() {
        return receiptPaymentVerificationRepository.findAll(createSpecification());
    }

    private Specification<ReceiptPaymentVerification> createSpecification() {
        Specification<ReceiptPaymentVerification> spec = Specification.where(null);
        if (outgoingPaymentId.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("outgoingPaymentId"), outgoingPaymentId.get()));
        }
        return spec;
    }

}
