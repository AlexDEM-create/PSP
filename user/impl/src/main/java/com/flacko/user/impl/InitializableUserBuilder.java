package com.flacko.user.impl;

import com.flacko.user.service.User;
import com.flacko.user.service.UserBuilder;

public interface InitializableUserBuilder extends UserBuilder {

    UserBuilder initializeNew();

    UserBuilder initializeExisting(User existingUser);

}
