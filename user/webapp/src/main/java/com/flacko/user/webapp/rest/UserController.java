package com.flacko.user.webapp.rest;

import com.auth0.jwt.JWT;
import com.flacko.common.exception.UserNotFoundException;
import com.flacko.common.role.UserRole;
import com.flacko.security.SecurityConfig;
import com.flacko.user.service.User;
import com.flacko.user.service.UserBuilder;
import com.flacko.user.service.UserListBuilder;
import com.flacko.user.service.UserService;
import com.flacko.user.service.exception.UserLoginAlreadyInUseException;
import com.flacko.user.service.exception.UserMissingRequiredAttributeException;
import com.flacko.user.service.exception.UserWeakPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private static final String BANNED = "banned";
    private static final String ROLE = "role";
    private static final String ARCHIVED = "archived";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

    private final UserService userService;
    private final UserRestMapper userRestMapper;

    @GetMapping
    public List<UserResponse> list(@RequestParam(BANNED) Optional<Boolean> banned,
                                   @RequestParam(ROLE) Optional<UserRole> role,
                                   @RequestParam(ARCHIVED) Optional<Boolean> archived,
                                   @RequestParam(value = LIMIT, defaultValue = "10") Integer limit,
                                   @RequestParam(value = OFFSET, defaultValue = "0") Integer offset) {
        UserListBuilder builder = userService.list();
        banned.ifPresent(builder::withBanned);
        role.ifPresent(builder::withRole);
        archived.ifPresent(builder::withArchived);
        return builder.build().stream()
                .map(userRestMapper::mapModelToResponse)
                .skip(offset)
                .limit(limit)
                .sorted(Comparator.comparing(UserResponse::createdDate).reversed())
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public UserResponse get(@PathVariable String userId) throws UserNotFoundException {
        return userRestMapper.mapModelToResponse(userService.get(userId));
    }

    @GetMapping("/me")
    public UserResponse getMe(@RequestHeader("Authorization") String tokenWithPrefix) throws UserNotFoundException {
        String token = tokenWithPrefix.substring(SecurityConfig.TOKEN_PREFIX.length());
        String login = JWT.decode(token).getSubject();
        return userRestMapper.mapModelToResponse(userService.getByLogin(login));
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

    @PatchMapping("/{userId}/ban")
    public UserResponse ban(@PathVariable String userId)
            throws UserNotFoundException, UserMissingRequiredAttributeException, UserLoginAlreadyInUseException,
            UserWeakPasswordException {
        UserBuilder builder = userService.update(userId);
        builder.withBanned();
        User user = builder.build();
        return userRestMapper.mapModelToResponse(user);
    }

}
