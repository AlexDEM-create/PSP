package com.flacko.appeal.service.exception;

import com.flacko.appeal.service.AppealState;

public class AppealIllegalStateTransitionException extends Exception {

    public AppealIllegalStateTransitionException(String id, AppealState currentState, AppealState newState) {
        super(String.format("Appeal %s state cannot be changed from %s to %s", id, currentState, newState));
    }

}
