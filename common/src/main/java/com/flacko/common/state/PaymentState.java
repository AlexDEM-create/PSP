package com.flacko.common.state;

import java.util.EnumSet;
import java.util.Set;

public enum PaymentState {

    INITIATED,
    VERIFYING,
    VERIFIED,
    FAILED_TO_VERIFY,
    DISPUTED,
    VERIFICATION_EXPIRED;

    private Set<PaymentState> nextPossibleStates;

    static {
        INITIATED.nextPossibleStates = EnumSet.of(INITIATED, VERIFYING);
        VERIFYING.nextPossibleStates = EnumSet.of(INITIATED, VERIFIED, FAILED_TO_VERIFY);
        VERIFIED.nextPossibleStates = EnumSet.of(DISPUTED);
        FAILED_TO_VERIFY.nextPossibleStates = EnumSet.of(INITIATED, DISPUTED);
        DISPUTED.nextPossibleStates = EnumSet.of(VERIFIED, FAILED_TO_VERIFY, INITIATED);
        VERIFICATION_EXPIRED.nextPossibleStates = EnumSet.of(INITIATED, DISPUTED);
    }

    public boolean canChangeTo(PaymentState newState) {
        return nextPossibleStates.contains(newState);
    }

}
