package com.flacko.trader.TraderTeam;

import com.flacko.trader.TraderTeam.exception.TraderTeamMissingRequiredAttributeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TraderTeamBuilderImpl implements InitializableTraderTeamBuilder {

    private TraderTeamPojo.TraderTeamPojoBuilder pojoBuilder;

    @Override
    public TraderTeamBuilder initializeNew() {
        pojoBuilder = TraderTeamPojo.builder();
        return this;
    }

    @Override
    public TraderTeamBuilder initializeExisting(TraderTeam existingTraderTeam) {
        pojoBuilder = TraderTeamPojo.builder()
                .id(existingTraderTeam.getId())
                .name(existingTraderTeam.getName())
                .isKickedOut(existingTraderTeam.getIsKickedOut());
        return this;
    }

    @Override
    public TraderTeamBuilder withId(String id) {
        pojoBuilder.id(id);
        return this;
    }

    @Override
    public TraderTeamBuilder withName(String name) {
        pojoBuilder.name(name);
        return this;
    }

    @Override
    public TraderTeamBuilder withIsKickedOut(Boolean isKickedOut) {
        pojoBuilder.isKickedOut(isKickedOut);
        return this;
    }

    @Override
    public TraderTeamBuilder withArchived() {
        return null;
    }

    @Override
    public TraderTeam build() throws TraderTeamMissingRequiredAttributeException {
        TraderTeamPojo traderTeam = pojoBuilder.build();
        validate(traderTeam);
        return traderTeam;
    }

    private void validate(TraderTeamPojo traderTeam) throws TraderTeamMissingRequiredAttributeException {
        // validation logic here
    }

}
