package com.flacko.common.exception;

public class NoEligibleTraderTeamsException extends NotFoundException {

    public NoEligibleTraderTeamsException() {
        super("No eligible trader teams");
    }

}
