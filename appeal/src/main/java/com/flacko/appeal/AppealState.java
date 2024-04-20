package com.flacko.appeal;

import java.util.EnumSet;
import java.util.Set;

public enum AppealState {

    INITIATED,
    UNDER_REVIEW,
    RESOLVED,
    REJECTED;

    private Set<AppealState> nextPossibleStates;

    static {
        INITIATED.nextPossibleStates = EnumSet.of(UNDER_REVIEW);
        UNDER_REVIEW.nextPossibleStates = EnumSet.of(RESOLVED, REJECTED);
        RESOLVED.nextPossibleStates = Set.of();
        REJECTED.nextPossibleStates = Set.of();
    }

    public boolean canChangeTo(AppealState newState) {
        return nextPossibleStates.contains(newState);
    }

}
