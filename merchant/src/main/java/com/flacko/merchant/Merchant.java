package com.flacko.merchant;


import java.time.Instant;
import java.util.Optional;

public interface Merchant {

    Long getPrimaryKey();

    String getId();

    String getName();

    String getUserId();

    Instant getCreatedDate();

    Instant getUpdatedDate();

    Optional<Instant> getDeletedDate();

}
