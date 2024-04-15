package com.flacko.auth.user;

import java.time.Instant;
import java.util.Optional;

public interface User {

    String getId();

    String getLogin();

    String getPassword();

    Role getRole();

    boolean isBanned();

    Instant getCreatedDate();

    Instant getUpdatedDate();

    Optional<Instant> getDeletedDate();

}
