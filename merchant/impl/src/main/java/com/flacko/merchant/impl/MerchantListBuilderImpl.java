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

    private Optional<Country> country = Optional.empty();
    private Optional<Boolean> outgoingTrafficStopped = Optional.empty();

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
    public List<Merchant> build() {
        return merchantRepository.findAll(createSpecification());
    }

    private Specification<Merchant> createSpecification() {
        Specification<Merchant> spec = Specification.where(null);
        if (country.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("country"), country.get()));
        }
        if (outgoingTrafficStopped.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("outgoingTrafficStopped"), outgoingTrafficStopped.get()));
        }
        return spec;
    }

}
