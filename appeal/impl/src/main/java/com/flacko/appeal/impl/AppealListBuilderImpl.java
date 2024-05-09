package com.flacko.appeal.impl;

import com.flacko.appeal.service.Appeal;
import com.flacko.appeal.service.AppealListBuilder;
import com.flacko.appeal.service.AppealSource;
import com.flacko.appeal.service.AppealState;
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
public class AppealListBuilderImpl implements AppealListBuilder {

    private final AppealRepository appealRepository;

    private Optional<String> paymentId = Optional.empty();
    private Optional<AppealSource> source = Optional.empty();
    private Optional<AppealState> currentState = Optional.empty();

    @Override
    public AppealListBuilder withPaymentId(String paymentId) {
        this.paymentId = Optional.of(paymentId);
        return this;
    }

    @Override
    public AppealListBuilder withSource(AppealSource source) {
        this.source = Optional.of(source);
        return this;
    }

    @Override
    public AppealListBuilder withCurrentState(AppealState currentState) {
        this.currentState = Optional.of(currentState);
        return this;
    }

    @Override
    public List<Appeal> build() {
        return appealRepository.findAll(createSpecification());
    }

    private Specification<Appeal> createSpecification() {
        Specification<Appeal> spec = Specification.where(null);
        if (paymentId.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("paymentId"), paymentId.get()));
        }
        if (source.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("source"), source.get()));
        }
        if (currentState.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("currentState"), currentState.get()));
        }
        return spec;
    }

}
