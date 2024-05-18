package com.flacko.merchant.impl;

import com.flacko.balance.service.BalanceService;
import com.flacko.balance.service.BalanceType;
import com.flacko.balance.service.EntityType;
import com.flacko.common.country.Country;
import com.flacko.common.currency.Currency;
import com.flacko.common.exception.*;
import com.flacko.common.id.IdGenerator;
import com.flacko.common.operation.CrudOperation;
import com.flacko.merchant.service.Merchant;
import com.flacko.merchant.service.MerchantBuilder;
import com.flacko.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MerchantBuilderImpl implements InitializableMerchantBuilder {

    private final Instant now = Instant.now();

    private final MerchantRepository merchantRepository;
    private final UserService userService;
    private final BalanceService balanceService;

    private MerchantPojo.MerchantPojoBuilder pojoBuilder;
    private CrudOperation crudOperation;

    @Override
    public MerchantBuilder initializeNew() {
        crudOperation = CrudOperation.CREATE;
        pojoBuilder = MerchantPojo.builder()
                .id(new IdGenerator().generateId())
                .outgoingTrafficStopped(false);
        return this;
    }


    @Override
    public MerchantBuilder initializeExisting(Merchant existingMerchant) {
        crudOperation = CrudOperation.UPDATE;
        pojoBuilder = MerchantPojo.builder()
                .primaryKey(existingMerchant.getPrimaryKey())
                .id(existingMerchant.getId())
                .name(existingMerchant.getName())
                .userId(existingMerchant.getUserId())
                .country(existingMerchant.getCountry())
                .incomingFeeRate(existingMerchant.getIncomingFeeRate())
                .outgoingFeeRate(existingMerchant.getOutgoingFeeRate())
                .outgoingTrafficStopped(existingMerchant.isOutgoingTrafficStopped())
                .createdDate(existingMerchant.getCreatedDate())
                .updatedDate(now)
                .deletedDate(existingMerchant.getDeletedDate().orElse(null));
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
    public MerchantBuilder withCountry(Country country) {
        pojoBuilder.country(country);
        return this;
    }

    @Override
    public MerchantBuilder withIncomingFeeRate(BigDecimal incomingFeeRate) {
        pojoBuilder.incomingFeeRate(incomingFeeRate);
        return this;
    }

    @Override
    public MerchantBuilder withOutgoingFeeRate(BigDecimal outgoingFeeRate) {
        pojoBuilder.outgoingFeeRate(outgoingFeeRate);
        return this;
    }

    @Override
    public MerchantBuilder withOutgoingTrafficStopped(boolean outgoingTrafficStopped) {
        pojoBuilder.outgoingTrafficStopped(outgoingTrafficStopped);
        return this;
    }

    @Override
    public MerchantBuilder withArchived() {
        pojoBuilder.deletedDate(now);
        return this;
    }

    @Override
    public Merchant build() throws MerchantMissingRequiredAttributeException, UserNotFoundException,
            MerchantInvalidFeeRateException, TraderTeamNotFoundException, MerchantNotFoundException,
            BalanceMissingRequiredAttributeException, BalanceInvalidCurrentBalanceException {
        MerchantPojo merchant = pojoBuilder.build();
        validate(merchant);
        merchantRepository.save(merchant);

        if (crudOperation == CrudOperation.CREATE) {
            balanceService.create()
                    .withEntityId(merchant.getId())
                    .withEntityType(EntityType.MERCHANT)
                    .withType(BalanceType.INCOMING)
                    .withCurrency(parseCurrency(merchant.getCountry()))
                    .build();

            balanceService.create()
                    .withEntityId(merchant.getId())
                    .withEntityType(EntityType.MERCHANT)
                    .withType(BalanceType.OUTGOING)
                    .withCurrency(parseCurrency(merchant.getCountry()))
                    .build();
        }

        return merchant;
    }

    private void validate(MerchantPojo pojo) throws MerchantMissingRequiredAttributeException, UserNotFoundException,
            MerchantInvalidFeeRateException {
        if (pojo.getId() == null || pojo.getId().isBlank()) {
            throw new MerchantMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getName() == null || pojo.getName().isBlank()) {
            throw new MerchantMissingRequiredAttributeException("name", Optional.of(pojo.getName()));
        }
        if (pojo.getUserId() == null || pojo.getUserId().isBlank()) {
            throw new MerchantMissingRequiredAttributeException("userId", Optional.of(pojo.getId()));
        } else {
            userService.get(pojo.getUserId());
        }
        if (pojo.getCountry() == null) {
            throw new MerchantMissingRequiredAttributeException("country", Optional.of(pojo.getId()));
        }
        if (pojo.getIncomingFeeRate() == null) {
            throw new MerchantMissingRequiredAttributeException("incomingFeeRate", Optional.of(pojo.getId()));
        } else if (pojo.getIncomingFeeRate().compareTo(BigDecimal.ZERO) <= 0) {
            throw new MerchantInvalidFeeRateException("Incoming", pojo.getId());
        }
        if (pojo.getOutgoingFeeRate() == null) {
            throw new MerchantMissingRequiredAttributeException("outgoingFeeRate", Optional.of(pojo.getId()));
        } else if (pojo.getOutgoingFeeRate().compareTo(BigDecimal.ZERO) <= 0) {
            throw new MerchantInvalidFeeRateException("Outgoing", pojo.getId());
        }
    }

    private Currency parseCurrency(Country country) {
        return switch (country) {
            case RUSSIA -> Currency.RUB;
            case UZBEKISTAN -> Currency.UZS;
        };
    }

}
