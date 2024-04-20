package com.flacko.trader.team.exception;

public class TraderTeamNotFoundException extends Exception {

    public TraderTeamNotFoundException(String id) {
        super(String.format("Terminal %s not found", id));
    }

}
