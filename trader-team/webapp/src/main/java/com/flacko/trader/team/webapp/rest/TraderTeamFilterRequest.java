package com.flacko.trader.team.webapp.rest;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public record TraderTeamFilterRequest(@RequestParam(ONLINE) Optional<Boolean> online,
                                      @RequestParam(KICKED_OUT) Optional<Boolean> kickedOut,
                                      @RequestParam(LEADER_ID) Optional<String> leaderId) {

    private static final String ONLINE = "online";
    private static final String KICKED_OUT = "kicked_out";
    private static final String LEADER_ID = "leader_id";

}
