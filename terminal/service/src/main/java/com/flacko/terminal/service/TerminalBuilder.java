package com.flacko.terminal.service;

import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.terminal.service.exception.TerminalMissingRequiredAttributeException;

public interface TerminalBuilder {

    TerminalBuilder withTraderTeamId(String traderTeamId);

    TerminalBuilder withVerified();

    TerminalBuilder withOnline(boolean online);

    TerminalBuilder withModel(String model);

    TerminalBuilder withOperatingSystem(String operatingSystem);

    TerminalBuilder withArchived();

    Terminal build() throws TerminalMissingRequiredAttributeException, TraderTeamNotFoundException;

}
