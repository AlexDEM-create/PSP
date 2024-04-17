package com.flacko.trader.TraderTeam.exception;

public class TraderTeamNotFoundException extends Exception {
    public TraderTeamNotFoundException(String id) {
        super("Trader team with id " + id + " not found");
    }
}
