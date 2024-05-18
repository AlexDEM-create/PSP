package com.flacko.terminal.service;

import java.util.List;

public interface TerminalListBuilder {

    TerminalListBuilder withTraderTeamId(String traderTeamId);

    TerminalListBuilder withVerified(Boolean verified);

    TerminalListBuilder withEnabled(Boolean enabled);

    TerminalListBuilder withOnline(Boolean online);

    TerminalListBuilder withArchived(Boolean archived);

    List<Terminal> build();

}
