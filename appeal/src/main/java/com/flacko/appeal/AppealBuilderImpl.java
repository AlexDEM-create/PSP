
package com.flacko.appeal;

import com.flacko.appeal.exception.AppealMissingRequiredAttributeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class AppealBuilderImpl implements InitializableAppealBuilder {

    private AppealPojo.AppealPojoBuilder pojoBuilder;
    private AppealStatus appealStatus;

    @Override
    public AppealBuilder initializeNew() {
        pojoBuilder = AppealPojo.builder();
        return this;
    }

    @Override
    public AppealBuilder initializeExisting(Appeal existingAppeal) {
        pojoBuilder = AppealPojo.builder()
                .id(existingAppeal.getId())
                .appealStatus((AppealStatus) existingAppeal.getAppealStatus())
                .paymentId(existingAppeal.getPaymentId());
        return this;
    }

    @Override
    public InitializableAppealBuilder withAppealStatus(AppealStatus appealStatus) {
        this.appealStatus = appealStatus;
        return this;
    }

    @Override
    public AppealBuilder withPaymentId(String paymentId) {
        pojoBuilder.paymentId(paymentId);
        return this;
    }

    @Override
    public Appeal build() throws AppealMissingRequiredAttributeException {
        AppealPojo appeal = pojoBuilder.appealStatus(appealStatus).build();
        validate(appeal);
        return appeal;
    }

    private void validate(AppealPojo pojo) throws AppealMissingRequiredAttributeException {
        if (pojo.getId() == null || pojo.getId().isEmpty()) {
            throw new AppealMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getPaymentId() == null || pojo.getPaymentId().isEmpty()) {
            throw new AppealMissingRequiredAttributeException("paymentId", Optional.of(pojo.getId()));
        }
        if (pojo.getAppealStatus() == null) {
            throw new AppealMissingRequiredAttributeException("appealStatus", Optional.of(pojo.getId()));
        }
    }

}
