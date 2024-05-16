package com.flacko.user.impl;

import com.flacko.common.id.IdGenerator;
import com.flacko.common.role.UserRole;
import com.flacko.user.service.User;
import com.flacko.user.service.UserBuilder;
import com.flacko.user.service.exception.UserLoginAlreadyInUseException;
import com.flacko.user.service.exception.UserMissingRequiredAttributeException;
import com.flacko.user.service.exception.UserWeakPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class UserBuilderImpl implements InitializableUserBuilder {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^.{10,}$");

    private final Instant now = Instant.now();

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private UserPojo.UserPojoBuilder pojoBuilder;

    @Override
    public UserBuilder initializeNew() {
        pojoBuilder = UserPojo.builder()
                .id(new IdGenerator().generateId())
                .banned(false);
        return this;
    }

    @Override
    public UserBuilder initializeExisting(User existingUser) {
        pojoBuilder = UserPojo.builder()
                .primaryKey(existingUser.getPrimaryKey())
                .id(existingUser.getId())
                .login(existingUser.getLogin())
                .password(existingUser.getPassword())
                .role(existingUser.getRole())
                .banned(existingUser.isBanned())
                .createdDate(existingUser.getCreatedDate())
                .updatedDate(now)
                .deletedDate(existingUser.getDeletedDate().orElse(null));
        return this;
    }

    @Override
    public UserBuilder withLogin(String login) {
        pojoBuilder.login(login);
        return this;
    }

    @Override
    public UserBuilder withPassword(String password) {
        pojoBuilder.password(passwordEncoder.encode(password));
        return this;
    }

    @Override
    public UserBuilder withRole(UserRole role) {
        pojoBuilder.role(role);
        return this;
    }

    @Override
    public UserBuilder withBanned() {
        pojoBuilder.banned(true);
        return this;
    }

    @Override
    public UserBuilder withArchived() {
        pojoBuilder.deletedDate(now);
        return this;
    }

    @Transactional
    @Override
    public User build() throws UserMissingRequiredAttributeException, UserLoginAlreadyInUseException,
            UserWeakPasswordException {
        UserPojo user = pojoBuilder.build();
        validate(user);
        userRepository.save(user);
        return user;
    }

    private void validate(UserPojo pojo) throws UserMissingRequiredAttributeException, UserWeakPasswordException, UserLoginAlreadyInUseException {
        if (pojo.getId() == null || pojo.getId().isBlank()) {
            throw new UserMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getLogin() == null || pojo.getLogin().isBlank()) {
            throw new UserMissingRequiredAttributeException("login", Optional.of(pojo.getId()));
        } else {
            Optional<User> user = userRepository.findByLogin(pojo.getLogin());
            if (user.isPresent()) {
                throw new UserLoginAlreadyInUseException(pojo.getLogin());
            }
        }
        if (pojo.getPassword() == null || pojo.getPassword().isBlank()) {
            throw new UserMissingRequiredAttributeException("password", Optional.of(pojo.getId()));
        } else if (!PASSWORD_PATTERN.matcher(pojo.getPassword()).matches()) {
            throw new UserWeakPasswordException(pojo.getLogin());
        }
        if (pojo.getRole() == null) {
            throw new UserMissingRequiredAttributeException("role", Optional.of(pojo.getId()));
        }
    }

}
