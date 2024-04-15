package com.flacko.trader;

import com.flacko.trader.exception.TraderMissingRequiredAttributeException;
import lombok.Data;

import java.util.Optional;

@Data
public class TraderImpl implements Trader, TraderBuilder {
    public String id;
    public String name;
    public String userId;
    public String traderTeamId;

    @Override
    public TraderBuilder withId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public TraderBuilder withName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Trader build() throws TraderMissingRequiredAttributeException {
        if (name == null || userId == null) {
            throw new TraderMissingRequiredAttributeException("name or userId", Optional.ofNullable(id));
        }
        return this;
    }
}
