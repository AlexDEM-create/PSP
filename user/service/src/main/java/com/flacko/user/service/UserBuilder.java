package com.flacko.user.service;

import com.flacko.user.service.exception.UserLoginAlreadyInUseException;
import com.flacko.user.service.exception.UserMissingRequiredAttributeException;
import com.flacko.user.service.exception.UserWeakPasswordException;

public interface UserBuilder {

    UserBuilder withLogin(String login);

    UserBuilder withPassword(String password);

    UserBuilder withRole(UserRole role);

    UserBuilder withBanned();

    UserBuilder withArchived();

    User build() throws UserMissingRequiredAttributeException, UserLoginAlreadyInUseException,
            UserWeakPasswordException;

}
