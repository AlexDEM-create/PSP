package com.flacko.terminal.impl;

import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.id.IdGenerator;
import com.flacko.terminal.service.Terminal;
import com.flacko.terminal.service.TerminalBuilder;
import com.flacko.terminal.service.exception.TerminalMissingRequiredAttributeException;
import com.flacko.trader.team.service.TraderTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TerminalBuilderImpl implements InitializableTerminalBuilder {

    private final Instant now = Instant.now();

    private final TerminalRepository terminalRepository;
    private final TraderTeamService traderTeamService;

    private TerminalPojo.TerminalPojoBuilder pojoBuilder;

    @Override
    public TerminalBuilder initializeNew() {
        pojoBuilder = TerminalPojo.builder()
                .id(new IdGenerator().generateId())
                .verified(false)
                .online(false);
        return this;
    }

    @Override
    public TerminalBuilder initializeExisting(Terminal existingTerminal) {
        // need to solve the problem with primary key
        pojoBuilder = TerminalPojo.builder()
                .primaryKey(existingTerminal.getPrimaryKey())
                .id(existingTerminal.getId())
                .traderTeamId(existingTerminal.getTraderTeamId())
                .verified(existingTerminal.isVerified())
                .model(existingTerminal.getModel().orElse(null))
                .operatingSystem(existingTerminal.getOperatingSystem().orElse(null))
                .createdDate(existingTerminal.getCreatedDate())
                .updatedDate(now)
                .deletedDate(existingTerminal.getDeletedDate().orElse(null));
        return this;
    }

    @Override
    public TerminalBuilder withTraderTeamId(String traderTeamId) {
        pojoBuilder.traderTeamId(traderTeamId);
        return this;
    }

    @Override
    public TerminalBuilder withVerified() {
        pojoBuilder.verified(true);
        return this;
    }

    @Override
    public TerminalBuilder withOnline(boolean online) {
        pojoBuilder.online(online);
        return this;
    }

    @Override
    public TerminalBuilder withModel(String model) {
        pojoBuilder.model(model);
        return this;
    }

    @Override
    public TerminalBuilder withOperatingSystem(String operatingSystem) {
        pojoBuilder.operatingSystem(operatingSystem);
        return this;
    }

    @Override
    public TerminalBuilder withArchived() {
        pojoBuilder.deletedDate(now);
        return this;
    }

    @Override
    public Terminal build() throws TerminalMissingRequiredAttributeException, TraderTeamNotFoundException {
        TerminalPojo terminal = pojoBuilder.build();
        validate(terminal);
        terminalRepository.save(terminal);
        return terminal;
    }

    private void validate(TerminalPojo pojo) throws TerminalMissingRequiredAttributeException,
            TraderTeamNotFoundException {
        if (pojo.getId() == null || pojo.getId().isBlank()) {
            throw new TerminalMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getTraderTeamId() == null || pojo.getTraderTeamId().isBlank()) {
            throw new TerminalMissingRequiredAttributeException("traderTeamId", Optional.of(pojo.getId()));
        } else {
            traderTeamService.get(pojo.getTraderTeamId());
        }
    }

}

