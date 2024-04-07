package com.flacko.auth.user;

public interface UserBuilder {

    UserBuilder withId(String id);

    UserBuilder withLogin(String login);

    UserBuilder withPassword(String password);

    UserBuilder withRole(Role role);

}
