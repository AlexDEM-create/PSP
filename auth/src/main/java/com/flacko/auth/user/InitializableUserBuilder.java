package com.flacko.auth.user;

public interface InitializableUserBuilder extends UserBuilder {

    UserBuilder initializeNew();

    UserBuilder initializeExisting(User existingUser);

}
