package com.flacko.merchant;

import com.flacko.merchant.exception.MerchantMissingRequiredAttributeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MerchantBuilderImpl implements InitializableMerchantBuilder {

    private final Instant now = Instant.now();

    private MerchantPojo.MerchantPojoBuilder pojoBuilder;

    @Override
    public MerchantBuilder initializeNew() {
        pojoBuilder = MerchantPojo.builder()
                .createdDate(now)
                .updatedDate(now);
        return this;
    }


    @Override
    public MerchantBuilder initializeExisting(Merchant existingMerchant) {
        pojoBuilder = MerchantPojo.builder()
                .id(existingMerchant.getId())
                .name(existingMerchant.getName())
                .createdDate(existingMerchant.getCreatedDate())
                .updatedDate(now);
        pojoBuilder.userId(existingMerchant.getUserId().orElse(null));
        return this;
    }

    @Override
    public MerchantBuilder withId(String id) {
        pojoBuilder.id(id);
        return this;
    }

    @Override
    public MerchantBuilder withName(String name) {
        pojoBuilder.name(name);
        return this;
    }

    @Override
    public MerchantBuilder withUserId(String userId) {
        pojoBuilder.userId(userId);
        return this;
    }

    @Override
    public MerchantBuilder withArchived() {
        pojoBuilder.deletedDate(now);
        return this;
    }

    @Override
    public Merchant build() throws MerchantMissingRequiredAttributeException {
        MerchantPojo merchant = pojoBuilder.build();
        validate(merchant);
        return merchant;
    }

    private void validate(MerchantPojo merchant) throws MerchantMissingRequiredAttributeException {
        if (merchant.getId() == null || merchant.getId().isEmpty()) {
            throw new MerchantMissingRequiredAttributeException("id", Optional.empty());
        }
        if (merchant.getName() == null || merchant.getName().isEmpty()) {
            throw new MerchantMissingRequiredAttributeException("name", Optional.of(merchant.getName()));
        }
        if (merchant.getUserId() == null || merchant.getUserId().isEmpty()) {
            throw new MerchantMissingRequiredAttributeException("userId", Optional.of(merchant.getId()));
        }
    }
}
