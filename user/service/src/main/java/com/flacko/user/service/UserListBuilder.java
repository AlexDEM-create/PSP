package com.flacko.user.service;

import com.flacko.common.role.UserRole;

import java.util.List;

public interface UserListBuilder {

    UserListBuilder withBanned(Boolean banned);

    UserListBuilder withRole(UserRole role);

    UserListBuilder withArchived(Boolean archived);

    List<User> build();

}
