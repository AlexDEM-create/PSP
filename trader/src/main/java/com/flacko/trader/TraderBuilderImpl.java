package com.flacko.trader;

import com.flacko.trader.exception.TraderMissingRequiredAttributeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TraderBuilderImpl implements InitializableTraderBuilder {

    private TraderPojo.TraderPojoBuilder pojoBuilder;

    @Override
    public TraderBuilder initializeNew() {
        pojoBuilder = TraderPojo.builder();
        return this;
    }

    @Override
    public TraderBuilder initializeExisting(Trader existingTrader) {
        pojoBuilder = TraderPojo.builder()
                .id(existingTrader.getId())
                .userId(existingTrader.getUserId())
                .traderTeamId(existingTrader.getTraderTeamId());
        pojoBuilder.userId(existingTrader.getUserId());

        return this;
    }

    @Override
    public TraderBuilder withId(String id) {
        pojoBuilder.id(id);
        return this;
    }

    @Override
    public TraderBuilder withUserId(String userId) {
        pojoBuilder.userId(userId);
        return this;
    }

    @Override
    public TraderBuilder withArchived() {
        return null;
    }

    @Override
    public TraderBuilder withTraderTeamId(String traderTeamId) {
        pojoBuilder.traderTeamId(traderTeamId);
        return this;
    }

    @Override
    public Trader build() throws TraderMissingRequiredAttributeException {
        TraderPojo trader = pojoBuilder.build();
        validate(trader);
        return trader;
    }

    private void validate(TraderPojo trader) throws TraderMissingRequiredAttributeException {
        if (trader.getId() == null || trader.getId().isEmpty()) {
            throw new TraderMissingRequiredAttributeException("id", Optional.empty());
        }
        if (trader.getUserId() == null || trader.getUserId().isEmpty()) {
            throw new TraderMissingRequiredAttributeException("userId", Optional.of(trader.getId()));
        }
    }

}
