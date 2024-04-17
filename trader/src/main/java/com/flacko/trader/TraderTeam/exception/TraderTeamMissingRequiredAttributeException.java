package com.flacko.trader.TraderTeam.exception;

public class TraderTeamMissingRequiredAttributeException extends Exception {
    public TraderTeamMissingRequiredAttributeException(String attributeName) {
        super("TraderTeam attribute '" + attributeName + "' is missing");
    }
}
