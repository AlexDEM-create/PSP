package com.flacko.appeal;

import com.flacko.appeal.exception.AppealIllegalStateTransitionException;
import com.flacko.appeal.exception.AppealMissingRequiredAttributeException;
import com.flacko.auth.id.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
@RequiredArgsConstructor
public class AppealBuilderImpl implements InitializableAppealBuilder {

    private final AppealRepository appealRepository;

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
    public Appeal build() throws AppealMissingRequiredAttributeException {
        AppealPojo appeal = pojoBuilder.build();
        validate(appeal);
        appealRepository.save(appeal);
        return appeal;
    }

    private void validate(AppealPojo pojo) throws AppealMissingRequiredAttributeException {
        if (pojo.getId() == null || pojo.getId().isEmpty()) {
            throw new AppealMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getPaymentId() == null || pojo.getPaymentId().isEmpty()) {
            throw new AppealMissingRequiredAttributeException("paymentId", Optional.of(pojo.getId()));
        }
        if (pojo.getCurrentState() == null) {
            throw new AppealMissingRequiredAttributeException("currentState", Optional.of(pojo.getId()));
        }
    }

}
