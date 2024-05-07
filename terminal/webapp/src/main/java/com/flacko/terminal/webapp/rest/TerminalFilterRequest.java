package com.flacko.terminal.webapp.rest;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public record TerminalFilterRequest(@RequestParam(TRADER_TEAM_ID) Optional<String> traderTeamId,
                                    @RequestParam(VERIFIED) Optional<Boolean> verified,
                                    @RequestParam(ONLINE) Optional<Boolean> online,
                                    @RequestParam(name = LIMIT, defaultValue = "10") int limit,
                                    @RequestParam(name = OFFSET, defaultValue = "0") int offset) {

    private static final String TRADER_TEAM_ID = "trader_team_id";
    private static final String VERIFIED = "verified";
    private static final String ONLINE = "online";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

}
