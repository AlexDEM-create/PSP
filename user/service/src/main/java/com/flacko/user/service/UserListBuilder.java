package com.flacko.user.service;

import java.util.List;

public interface UserListBuilder {

    UserListBuilder withBanned(Boolean banned);

    UserListBuilder withRole(UserRole role);

    List<User> build();

}
