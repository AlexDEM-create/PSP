package com.flacko.auth.security.user;

import com.flacko.auth.security.user.exception.UserLoginAlreadyInUseException;
import com.flacko.auth.security.user.exception.UserMissingRequiredAttributeException;
import com.flacko.auth.security.user.exception.UserWeakPasswordException;

public interface UserBuilder {

    UserBuilder withLogin(String login);

    UserBuilder withPassword(String password);

    UserBuilder withRole(UserRole role);

    UserBuilder withBanned();

    UserBuilder withArchived();

    User build() throws UserMissingRequiredAttributeException, UserLoginAlreadyInUseException, UserWeakPasswordException;

}
