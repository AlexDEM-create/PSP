package com.flacko.terminal;

import com.flacko.terminal.exception.TerminalMissingRequiredAttributeException;

public interface TerminalBuilder {

    TerminalBuilder withTraderId(String traderId);

    TerminalBuilder withVerified();

    TerminalBuilder withModel(String model);

    TerminalBuilder withOperatingSystem(String operatingSystem);

    TerminalBuilder withArchived();

    Terminal build() throws TerminalMissingRequiredAttributeException;

}
