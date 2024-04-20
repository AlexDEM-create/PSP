package com.flacko.auth.security.user;

import com.flacko.auth.id.IdGenerator;
import com.flacko.auth.security.user.exception.UserMissingRequiredAttributeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
@RequiredArgsConstructor
public class UserBuilderImpl implements InitializableUserBuilder {

    private final Instant now = Instant.now();

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private UserPojo.UserPojoBuilder pojoBuilder;

    @Override
    public UserBuilder initializeNew() {
        pojoBuilder = UserPojo.builder()
                .id(new IdGenerator().generateId());
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
    public UserBuilder withRole(Role role) {
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

    @Override
    public User build() throws UserMissingRequiredAttributeException {
        UserPojo user = pojoBuilder.build();
        validate(user);
        userRepository.save(user);
        return user;
    }

    private void validate(UserPojo pojo) throws UserMissingRequiredAttributeException {
        if (pojo.getId() == null || pojo.getId().isEmpty()) {
            throw new UserMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getLogin() == null || pojo.getLogin().isEmpty()) {
            throw new UserMissingRequiredAttributeException("login", Optional.of(pojo.getId()));
        }
        if (pojo.getPassword() == null || pojo.getPassword().isEmpty()) {
            throw new UserMissingRequiredAttributeException("password", Optional.of(pojo.getId()));
        }
        if (pojo.getRole() == null) {
            throw new UserMissingRequiredAttributeException("role", Optional.of(pojo.getId()));
        }
    }

}
