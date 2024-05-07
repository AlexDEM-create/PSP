package com.flacko.card.webapp.rest;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public record CardFilterRequest(@RequestParam(BANK_ID) Optional<String> bankId,
                                @RequestParam(TRADER_TEAM_ID) Optional<String> traderTeamId,
                                @RequestParam(TERMINAL_ID) Optional<String> terminalId,
                                @RequestParam(BUSY) Optional<Boolean> busy,
                                @RequestParam(name = LIMIT, defaultValue = "10") int limit,
                                @RequestParam(name = OFFSET, defaultValue = "0") int offset) {

    private static final String BANK_ID = "bank_id";
    private static final String TRADER_TEAM_ID = "trader_team_id";
    private static final String TERMINAL_ID = "terminal_id";
    private static final String BUSY = "busy";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

}
