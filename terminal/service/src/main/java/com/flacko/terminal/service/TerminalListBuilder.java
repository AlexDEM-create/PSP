package com.flacko.terminal.service;

import java.util.List;

public interface TerminalListBuilder {

    TerminalListBuilder withTraderTeamId(String traderTeamId);

    TerminalListBuilder withVerified(Boolean verified);

    TerminalListBuilder withOnline(Boolean online);

    List<Terminal> build();

}
