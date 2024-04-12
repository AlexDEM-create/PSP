package com.flacko.trader;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TraderBuilderImpl extends TraderImpl implements InitializableTraderBuilder {

    @Override
    public TraderBuilder initializeNew() {
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
