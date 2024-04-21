package com.flacko.appeal;

import com.flacko.appeal.exception.AppealIllegalPaymentCurrentStateException;
import com.flacko.appeal.exception.AppealIllegalStateTransitionException;
import com.flacko.appeal.exception.AppealMissingRequiredAttributeException;
import com.flacko.auth.id.IdGenerator;
import com.flacko.payment.Payment;
import com.flacko.payment.PaymentService;
import com.flacko.payment.PaymentState;
import com.flacko.payment.exception.PaymentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
@RequiredArgsConstructor
public class AppealBuilderImpl implements InitializableAppealBuilder {

    private static final Set<PaymentState> ALLOWED_PAYMENT_STATES = Set.of(PaymentState.VERIFIED,
            PaymentState.FAILED_TO_VERIFY, PaymentState.VERIFICATION_EXPIRED);

    private final AppealRepository appealRepository;
    private final PaymentService paymentService;

    private AppealPojo.AppealPojoBuilder pojoBuilder;
    private String id;
    private AppealState currentState;

    @Override
    public AppealBuilder initializeNew() {
        pojoBuilder = AppealPojo.builder()
                .id(new IdGenerator().generateId())
                .currentState(AppealState.INITIATED);
        return this;
    }

    @Override
    public AppealBuilder initializeExisting(Appeal existingAppeal) {
        pojoBuilder = AppealPojo.builder()
                .primaryKey(existingAppeal.getPrimaryKey())
                .id(existingAppeal.getId())
                .paymentId(existingAppeal.getPaymentId())
                .currentState(existingAppeal.getCurrentState())
                .createdDate(existingAppeal.getCreatedDate())
                .updatedDate(Instant.now());
        id = existingAppeal.getId();
        currentState = existingAppeal.getCurrentState();
        return this;
    }

    @Override
    public AppealBuilder withPaymentId(String paymentId) {
        pojoBuilder.paymentId(paymentId);
        return this;
    }

    @Override
    public AppealBuilder withState(AppealState newState) throws AppealIllegalStateTransitionException {
        if (!currentState.canChangeTo(newState)) {
            throw new AppealIllegalStateTransitionException(id, currentState, newState);
        }
        pojoBuilder.currentState(newState);
        return this;
    }

    @Override
    public Appeal build() throws AppealMissingRequiredAttributeException, PaymentNotFoundException,
            AppealIllegalPaymentCurrentStateException {
        AppealPojo appeal = pojoBuilder.build();
        validate(appeal);
        appealRepository.save(appeal);
        return appeal;
    }

    private void validate(AppealPojo pojo) throws AppealMissingRequiredAttributeException, PaymentNotFoundException,
            AppealIllegalPaymentCurrentStateException {
        if (pojo.getId() == null || pojo.getId().isEmpty()) {
            throw new AppealMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getPaymentId() == null || pojo.getPaymentId().isEmpty()) {
            throw new AppealMissingRequiredAttributeException("paymentId", Optional.of(pojo.getId()));
        }
        Payment payment = paymentService.get(pojo.getPaymentId());
        if (!ALLOWED_PAYMENT_STATES.contains(payment.getCurrentState())) {
            throw new AppealIllegalPaymentCurrentStateException(pojo.getId(), pojo.getPaymentId(),
                    payment.getCurrentState());
        }
        if (pojo.getCurrentState() == null) {
            throw new AppealMissingRequiredAttributeException("currentState", Optional.of(pojo.getId()));
        }
    }

}
