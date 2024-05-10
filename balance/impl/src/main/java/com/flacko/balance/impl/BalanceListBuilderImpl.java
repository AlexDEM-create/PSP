package com.flacko.balance.impl;

import com.flacko.balance.service.Balance;
import com.flacko.balance.service.BalanceListBuilder;
import com.flacko.balance.service.EntityType;
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
public class BalanceListBuilderImpl implements BalanceListBuilder {

    private final BalanceRepository balanceRepository;

    private Optional<String> entityId = Optional.empty();
    private Optional<EntityType> entityType = Optional.empty();

    @Override
    public BalanceListBuilder withEntityId(String entityId) {
        this.entityId = Optional.of(entityId);
        return this;
    }

    @Override
    public BalanceListBuilder withEntityType(EntityType entityType) {
        this.entityType = Optional.of(entityType);
        return this;
    }

    @Override
    public List<Balance> build() {
        return balanceRepository.findAll(createSpecification());
    }

    private Specification<Balance> createSpecification() {
        Specification<Balance> spec = Specification.where(null);
        if (entityId.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("entityId"), entityId.get()));
        }
        if (entityType.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("entityType"), entityType.get()));
        }
        return spec;
    }

}
