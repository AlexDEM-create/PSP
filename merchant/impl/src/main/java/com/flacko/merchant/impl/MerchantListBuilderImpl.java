package com.flacko.merchant.impl;

import com.flacko.common.country.Country;
import com.flacko.merchant.service.Merchant;
import com.flacko.merchant.service.MerchantListBuilder;
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
public class MerchantListBuilderImpl implements MerchantListBuilder {

    private final MerchantRepository merchantRepository;

    private Optional<String> userId = Optional.empty();
    private Optional<Country> country = Optional.empty();
    private Optional<Boolean> outgoingTrafficStopped = Optional.empty();
    private Optional<Boolean> archived = Optional.empty();

    @Override
    public MerchantListBuilder withUserId(String userId) {
        this.userId = Optional.of(userId);
        return this;
    }

    @Override
    public MerchantListBuilder withCountry(Country country) {
        this.country = Optional.of(country);
        return this;
    }

    @Override
    public MerchantListBuilder withOutgoingTrafficStopped(boolean outgoingTrafficStopped) {
        this.outgoingTrafficStopped = Optional.of(outgoingTrafficStopped);
        return this;
    }

    @Override
    public MerchantListBuilder withArchived(Boolean archived) {
        this.archived = Optional.of(archived);
        return this;
    }

    @Override
    public List<Merchant> build() {
        return merchantRepository.findAll(createSpecification());
    }

    private Specification<Merchant> createSpecification() {
        Specification<Merchant> spec = Specification.where(null);
        if (userId.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("userId"), userId.get()));
        }
        if (country.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("country"), country.get()));
        }
        if (outgoingTrafficStopped.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("outgoingTrafficStopped"), outgoingTrafficStopped.get()));
        }
        if (archived.isPresent() && archived.get()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.isNotNull(root.get("deletedDate")));
        }
        return spec;
    }

}
