package com.flacko.user.service;

import com.flacko.common.exception.UserNotFoundException;

import java.util.List;

public interface UserService {

    UserBuilder create();

    UserBuilder update(String id) throws UserNotFoundException;

    List<User> list();

    User get(String id) throws UserNotFoundException;

    User getByLogin(String login) throws UserNotFoundException;

}
