package com.flacko.appeal.exception;

import com.flacko.appeal.AppealState;

public class AppealIllegalStateTransitionException extends Exception {

    public AppealIllegalStateTransitionException(String id, AppealState currentState, AppealState newState) {
        super(String.format("Appeal %s state cannot be changed from %s to %s", id, currentState, newState));
    }

}
