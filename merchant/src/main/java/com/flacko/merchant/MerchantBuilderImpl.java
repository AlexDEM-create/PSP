package com.flacko.merchant;

import com.flacko.auth.id.IdGenerator;
import com.flacko.merchant.MerchantBuilder;
import com.flacko.merchant.MerchantPojo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public abstract class MerchantBuilderImpl implements InitializableMerchantBuilder {

    private MerchantPojo.MerchantPojoBuilder pojoBuilder;

    @Override
    public MerchantBuilder initializeNew() {
        pojoBuilder = MerchantPojo.builder()
                .id(new IdGenerator().generateId());
        return this;
    }

    @Override
    public MerchantBuilder initializeExisting(Merchant existingMerchant) {
        pojoBuilder = MerchantPojo.builder()
                .id(existingMerchant.getId())
                .name(existingMerchant.getName())
                .userid(existingMerchant.getUserid())
                .createdDate(existingMerchant.getCreatedDate())
                .updatedDate(Instant.now());
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

    // остальные методы
}
