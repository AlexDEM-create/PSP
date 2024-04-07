package com.flacko.auth.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class UserBuilderImpl implements InitializableUserBuilder {

    private final PasswordEncoder passwordEncoder;

    private UserPojo.UserPojoBuilder pojoBuilder;

    @Override
    public UserBuilder initializeNew() {
        pojoBuilder = UserPojo.builder();
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
    public UserBuilder withId(String id) {
        pojoBuilder.id(id);
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

}
