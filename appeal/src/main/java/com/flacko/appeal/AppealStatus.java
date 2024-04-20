package com.flacko.appeal;

import java.util.EnumSet;
import java.util.Set;
public enum AppealStatus {

    NEW,
    IN_PROCESS,
    RESOLVED;

    private Set<AppealStatus> nextPossibleStates;

    static {
        NEW.nextPossibleStates = EnumSet.of(IN_PROCESS);
        IN_PROCESS.nextPossibleStates = EnumSet.of(RESOLVED);
        RESOLVED.nextPossibleStates = EnumSet.noneOf(AppealStatus.class);
    }

    public boolean canChangeTo(AppealStatus newState) {
        return nextPossibleStates.contains(newState);
    }
}
