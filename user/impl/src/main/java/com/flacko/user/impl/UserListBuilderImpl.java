package com.flacko.user.impl;

import com.flacko.user.service.User;
import com.flacko.user.service.UserListBuilder;
import com.flacko.user.service.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class UserListBuilderImpl implements UserListBuilder {

    private final UserRepository userRepository;

    private Optional<Boolean> banned = Optional.empty();
    private Optional<UserRole> role = Optional.empty();

    @Override
    public UserListBuilder withBanned(Boolean banned) {
        this.banned = Optional.of(banned);
        return this;
    }

    @Override
    public UserListBuilder withRole(UserRole role) {
        this.role = Optional.of(role);
        return this;
    }

    @Override
    public List<User> build() {
        return userRepository.findAll(createSpecification());
    }

    private Specification<User> createSpecification() {
        Specification<User> spec = Specification.where(null);
        if (banned.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("banned"), banned.get()));
        }
        if (role.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("role"), role.get()));
        }
        return spec;
    }

}
