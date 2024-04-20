package com.flacko.auth.security.user;

import java.time.Instant;
import java.util.Optional;

public interface User {

    Long getPrimaryKey();

    String getId();

    String getLogin();

    String getPassword();

    UserRole getRole();

    boolean isBanned();

    Instant getCreatedDate();

    Instant getUpdatedDate();

    Optional<Instant> getDeletedDate();

}
