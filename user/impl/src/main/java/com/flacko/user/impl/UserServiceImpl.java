package com.flacko.user.impl;

import com.flacko.user.service.User;
import com.flacko.user.service.UserBuilder;
import com.flacko.user.service.UserService;
import com.flacko.common.exception.UserNotFoundException;
import com.flacko.common.spring.ServiceLocator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ServiceLocator serviceLocator;

    @Override
    public List<User> list() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public User get(String id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    @Override
    public UserBuilder create() {
        return serviceLocator.create(InitializableUserBuilder.class)
                .initializeNew();
    }

    @Transactional
    @Override
    public UserBuilder update(String id) throws UserNotFoundException {
        User existingUser = get(id);
        return serviceLocator.create(InitializableUserBuilder.class)
                .initializeExisting(existingUser);
    }

    @Override
    public User getByLogin(String login) throws UserNotFoundException {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));
    }

}
