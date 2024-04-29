package com.flacko.bank.impl;

import com.flacko.bank.service.Bank;
import com.flacko.bank.service.BankBuilder;
import com.flacko.bank.service.exception.BankMissingRequiredAttributeException;
import com.flacko.common.id.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class BankBuilderImpl implements InitializableBankBuilder {

    private final Instant now = Instant.now();

    private final BankRepository bankRepository;

    private BankPojo.BankPojoBuilder pojoBuilder;

    @Override
    public BankBuilder initializeNew() {
        pojoBuilder = BankPojo.builder()
                .id(new IdGenerator().generateId());
        return this;
    }

    @Override
    public BankBuilder initializeExisting(Bank existingBank) {
        pojoBuilder = BankPojo.builder()
                .primaryKey(existingBank.getPrimaryKey())
                .id(existingBank.getId())
                .name(existingBank.getName())
                .country(existingBank.getCountry())
                .createdDate(existingBank.getCreatedDate())
                .updatedDate(now)
                .deletedDate(existingBank.getDeletedDate().orElse(null));
        return this;
    }

    @Override
    public BankBuilder withName(String name) {
        pojoBuilder.name(name);
        return this;
    }

    @Override
    public BankBuilder withCountry(String country) {
        pojoBuilder.country(country);
        return this;
    }

    @Override
    public BankBuilder withArchived() {
        pojoBuilder.deletedDate(now);
        return this;
    }

    @Override
    public Bank build() throws BankMissingRequiredAttributeException {
        BankPojo bank = pojoBuilder.build();
        validate(bank);
        bankRepository.save(bank);
        return bank;
    }

    private void validate(BankPojo pojo) throws BankMissingRequiredAttributeException {
        if (pojo.getId() == null || pojo.getId().isEmpty()) {
            throw new BankMissingRequiredAttributeException("id", Optional.empty());
        }
    }

}
