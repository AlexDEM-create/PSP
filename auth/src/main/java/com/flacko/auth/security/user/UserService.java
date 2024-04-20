package com.flacko.auth.security.user;

import com.flacko.auth.security.user.exception.UserNotFoundException;

import java.util.List;

public interface UserService {

    UserBuilder create();

    UserBuilder update(String id) throws UserNotFoundException;

    List<User> list();

    User get(String id) throws UserNotFoundException;

    User getByLogin(String login) throws UserNotFoundException;

}
