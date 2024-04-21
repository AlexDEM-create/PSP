package com.flacko.merchant;

import com.flacko.auth.id.IdGenerator;
import com.flacko.auth.security.user.UserService;
import com.flacko.auth.security.user.exception.UserNotFoundException;
import com.flacko.merchant.exception.MerchantInvalidFeeRateException;
import com.flacko.merchant.exception.MerchantMissingRequiredAttributeException;
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
                .primaryKey(existingMerchant.getPrimaryKey())
                .id(existingMerchant.getId())
                .name(existingMerchant.getName())
                .userId(existingMerchant.getUserId())
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
            MerchantInvalidFeeRateException {
        MerchantPojo merchant = pojoBuilder.build();
        validate(merchant);
        merchantRepository.save(merchant);
        return merchant;
    }

    private void validate(MerchantPojo pojo) throws MerchantMissingRequiredAttributeException, UserNotFoundException,
            MerchantInvalidFeeRateException {
        if (pojo.getId() == null || pojo.getId().isEmpty()) {
            throw new MerchantMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getName() == null || pojo.getName().isEmpty()) {
            throw new MerchantMissingRequiredAttributeException("name", Optional.of(pojo.getName()));
        }
        if (pojo.getUserId() == null || pojo.getUserId().isEmpty()) {
            throw new MerchantMissingRequiredAttributeException("userId", Optional.of(pojo.getId()));
        } else {
            userService.get(pojo.getUserId());
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

}
