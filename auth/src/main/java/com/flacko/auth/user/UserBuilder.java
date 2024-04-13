package com.flacko.auth.user;

import com.flacko.auth.user.exception.UserMissingRequiredAttributeException;

public interface UserBuilder {

    UserBuilder withLogin(String login);

    UserBuilder withPassword(String password);

    UserBuilder withRole(Role role);

    UserBuilder withBanned();

    UserBuilder withArchived();

    User build() throws UserMissingRequiredAttributeException;

}
