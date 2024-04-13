package com.flacko.terminal;

import com.flacko.terminal.exception.TerminalMissingRequiredAttributeException;
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

    private TerminalPojo.TerminalPojoBuilder pojoBuilder;

    @Override
    public TerminalBuilder initializeNew() {
        pojoBuilder = TerminalPojo.builder()
                .verified(false);
        return this;
    }

    @Override
    public TerminalBuilder initializeExisting(Terminal existingTerminal) {
        // need to solve the problem with primary key
        pojoBuilder = TerminalPojo.builder()
                .id(existingTerminal.getId())
                .traderId(existingTerminal.getTraderId())
                .model(existingTerminal.getModel().orElse(null))
                .operatingSystem(existingTerminal.getOperatingSystem().orElse(null))
                .updatedDate(now);
        return this;
    }

    @Override
    public TerminalBuilder withTraderId(String traderId) {
        pojoBuilder.traderId(traderId);
        return this;
    }

    @Override
    public TerminalBuilder withVerified() {
        pojoBuilder.verified(true);
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
    public Terminal build() throws TerminalMissingRequiredAttributeException {
        TerminalPojo terminal = pojoBuilder.build();
        validate(terminal);
        return terminal;
    }

    private void validate(TerminalPojo terminal) throws TerminalMissingRequiredAttributeException {
        if (terminal.getId() == null || terminal.getId().isEmpty()) {
            throw new TerminalMissingRequiredAttributeException("id", Optional.empty());
        }
        if (terminal.getTraderId() == null || terminal.getTraderId().isEmpty()) {
            throw new TerminalMissingRequiredAttributeException("traderId", Optional.of(terminal.getId()));
        }
    }

}

