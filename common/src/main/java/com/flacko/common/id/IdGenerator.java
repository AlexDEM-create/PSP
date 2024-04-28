package com.flacko.common.id;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Instant;

public final class IdGenerator {

    private static final int APPROXIMATE_BITS_PER_MAX_RADIX = 5;
    private static final int STRING_LENGTH = 14;
    private final SecureRandom random = new SecureRandom();

    public String generateId() {
        return Long.toString(Instant.now().getEpochSecond(), Character.MAX_RADIX)
                + new BigInteger(APPROXIMATE_BITS_PER_MAX_RADIX * STRING_LENGTH, random)
                .toString(Character.MAX_RADIX);
    }

}
