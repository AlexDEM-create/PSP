package com.flacko.merchant;

import java.time.Instant;
import java.util.Optional;

public interface Merchant {
    String getId();
    String getName();
    Optional<String> getUserId();
    Instant getCreatedDate();
    Instant getUpdatedDate();
    Optional<Instant> getDeletedDate();
}
