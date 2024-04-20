package com.flacko.auth.security.user;

public interface InitializableUserBuilder extends UserBuilder {

    UserBuilder initializeNew();

    UserBuilder initializeExisting(User existingUser);

}
