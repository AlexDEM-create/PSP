package com.flacko.trader;

import com.flacko.trader.exception.TraderMissingRequiredAttributeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TraderBuilderImpl   implements InitializableTraderBuilder {

    private final Instant now = Instant.now();

    private TraderPojo.TraderPojoBuilder pojoBuilder;

    @Override
    public TraderBuilder withId(String id) {
        return null;
    }

    @Override
    public TraderBuilder withName(String name) {
        return null;
    }

    @Override
    public TraderBuilder withUserId(String name) {
        return null;
    }

    @Override
    public TraderBuilder withArchived() {
        return null;
    }

    @Override
    public Trader build() throws TraderMissingRequiredAttributeException {
        return null;
    }

    @Override
    public TraderBuilder initializeNew() {
        pojoBuilder = TraderPojo.builder()
                .createdDate(now)
                .updatedDate(now);
        return this;
    }

    @Override
    public TraderBuilder initializeExisting(Trader existingTrader) {
        this.id = existingTrader.getId();
        this.name = existingTrader.getName();
        this.userId = existingTrader.getUserId();
        this.tradersTeam = existingTrader.getTradersTeam();
        return this;
    }
}




//    private final Instant now = Instant.now();
//
//    private MerchantPojo.MerchantPojoBuilder pojoBuilder;
//


//    @Override
    public MerchantBuilder initializeNew() {
        pojoBuilder = MerchantPojo.builder()
                .createdDate(now)
                .updatedDate(now);
        return this;
    }
//
//
//    @Override
//    public MerchantBuilder initializeExisting(Merchant existingMerchant) {
//        pojoBuilder = MerchantPojo.builder()
//                .id(existingMerchant.getId())
//                .name(existingMerchant.getName())
//                .createdDate(existingMerchant.getCreatedDate())
//                .updatedDate(now);
//        pojoBuilder.userId(existingMerchant.getUserId().orElse(null));
//        return this;
//    }
//
//    @Override
//    public MerchantBuilder withId(String id) {
//        pojoBuilder.id(id);
//        return this;
//    }
//
//    @Override
//    public MerchantBuilder withName(String name) {
//        pojoBuilder.name(name);
//        return this;
//    }
//
//    @Override
//    public MerchantBuilder withUserId(String userId) {
//        pojoBuilder.userId(userId);
//        return this;
//    }
//
//    @Override
//    public MerchantBuilder withArchived() {
//        pojoBuilder.deletedDate(now);
//        return this;
//    }
//
//    @Override
//    public Merchant build() throws MerchantMissingRequiredAttributeException {
//        MerchantPojo merchant = pojoBuilder.build();
//        validate(merchant);
//        return merchant;
//    }
//
//    private void validate(MerchantPojo merchant) throws MerchantMissingRequiredAttributeException {
//        if (merchant.getId() == null || merchant.getId().isEmpty()) {
//            throw new MerchantMissingRequiredAttributeException("id", Optional.empty());
//        }
//        if (merchant.getName() == null || merchant.getName().isEmpty()) {
//            throw new MerchantMissingRequiredAttributeException("name", Optional.of(merchant.getId()));
//        }
//        if (merchant.getUserId() == null || merchant.getUserId().isEmpty()) {
//            throw new MerchantMissingRequiredAttributeException("userId", Optional.of(merchant.getId()));
//        }
//    }
//}