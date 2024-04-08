package com.flacko.auth.user;

import com.flacko.auth.user.exception.UserMissingRequiredAttributeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class UserBuilderImpl implements InitializableUserBuilder {

    private final PasswordEncoder passwordEncoder;

    private UserPojo.UserPojoBuilder pojoBuilder;

    @Override
    public UserBuilder initializeNew() {
        pojoBuilder = UserPojo.builder()
                .isBanned(false);
        return this;
    }

    @Override
    public UserBuilder initializeExisting(User existingUser) {
        // need to solve the problem with primary key
        pojoBuilder = UserPojo.builder()
                .id(existingUser.getId())
                .login(existingUser.getLogin())
                .password(existingUser.getPassword())
                .role(existingUser.getRole());
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
    public UserBuilder withRole(Role role) {
        pojoBuilder.role(role);
        return this;
    }

    @Override
    public User build() throws UserMissingRequiredAttributeException {
        UserPojo user = pojoBuilder.build();
        validate(user);
        return user;
    }

    private void validate(UserPojo user) throws UserMissingRequiredAttributeException {
        if (user.getId() == null || user.getId().isEmpty()) {
            throw new UserMissingRequiredAttributeException("id", Optional.empty());
        }
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            throw new UserMissingRequiredAttributeException("login", Optional.of(user.getId()));
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new UserMissingRequiredAttributeException("password", Optional.of(user.getId()));
        }
        if (user.getRole() == null) {
            throw new UserMissingRequiredAttributeException("role", Optional.of(user.getId()));
        }
    }

}
