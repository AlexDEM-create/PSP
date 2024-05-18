package com.flacko.common.exception;

public class TraderTeamNotAllowedOnlineException extends Exception {

    public TraderTeamNotAllowedOnlineException(String id) {
        super(String.format("Trader team %s not allowed to go online", id));
    }

}
