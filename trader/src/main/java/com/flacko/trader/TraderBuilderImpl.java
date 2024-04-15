package com.flacko.trader;

import com.flacko.trader.exception.TraderMissingRequiredAttributeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TraderBuilderImpl   implements TraderBuilder {
    @Override
    public TraderBuilder withId(String id) {
        return null;
    }

    @Override
    public TraderBuilder withName(String name) {
        return null;
    }

    @Override
    public Trader build() throws TraderMissingRequiredAttributeException {
        return null;
    }

//    @Override
//    public TraderBuilder initializeNew() {
//        return this;
//    }
//
//    @Override
//    public TraderBuilder initializeExisting(Trader existingTrader) {
//        this.id = existingTrader.getId();
//        this.name = existingTrader.getName();
//        this.userId = existingTrader.getUserId();
//        this.tradersTeam = existingTrader.getTradersTeam();
//        return this;
//    }
}
