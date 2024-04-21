package com.flacko.auth.security.user.rest;

import com.flacko.auth.security.user.User;
import com.flacko.auth.security.user.UserBuilder;
import com.flacko.auth.security.user.UserService;
import com.flacko.auth.security.user.exception.UserLoginAlreadyInUseException;
import com.flacko.auth.security.user.exception.UserMissingRequiredAttributeException;
import com.flacko.auth.security.user.exception.UserNotFoundException;
import com.flacko.auth.security.user.exception.UserWeakPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserRestMapper userRestMapper;

    @GetMapping
    public List<UserResponse> list() {
        return userService.list()
                .stream()
                .map(userRestMapper::mapModelToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public UserResponse get(@PathVariable String userId) throws UserNotFoundException {
        return userRestMapper.mapModelToResponse(userService.get(userId));
    }

    @PostMapping
    public UserResponse create(@RequestBody UserCreateRequest userCreateRequest)
            throws UserMissingRequiredAttributeException, UserLoginAlreadyInUseException, UserWeakPasswordException {
        UserBuilder builder = userService.create();
        builder.withLogin(userCreateRequest.login())
                .withPassword(userCreateRequest.password())
                .withRole(userCreateRequest.role());
        User user = builder.build();
        return userRestMapper.mapModelToResponse(user);
    }

    @DeleteMapping("/{userId}")
    public UserResponse archive(@PathVariable String userId)
            throws UserNotFoundException, UserMissingRequiredAttributeException, UserLoginAlreadyInUseException,
            UserWeakPasswordException {
        UserBuilder builder = userService.update(userId);
        builder.withArchived();
        User user = builder.build();
        return userRestMapper.mapModelToResponse(user);
    }

    @PostMapping("/{userId}/ban")
    public UserResponse ban(@PathVariable String userId)
            throws UserNotFoundException, UserMissingRequiredAttributeException, UserLoginAlreadyInUseException,
            UserWeakPasswordException {
        UserBuilder builder = userService.update(userId);
        builder.withBanned();
        User user = builder.build();
        return userRestMapper.mapModelToResponse(user);
    }

}
