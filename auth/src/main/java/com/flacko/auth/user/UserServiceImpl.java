package com.flacko.auth.user;

import com.flacko.auth.spring.ServiceLocator;
import com.flacko.auth.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
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

    @Override
    public UserBuilder create() {
        return serviceLocator.create(InitializableUserBuilder.class)
                .initializeNew();
    }

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
