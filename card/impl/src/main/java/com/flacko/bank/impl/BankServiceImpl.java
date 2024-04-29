package com.flacko.bank.impl;

import com.flacko.bank.service.Bank;
import com.flacko.bank.service.BankBuilder;
import com.flacko.bank.service.BankService;
import com.flacko.common.exception.BankNotFoundException;
import com.flacko.common.spring.ServiceLocator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;
    private final ServiceLocator serviceLocator;

    @Override
    public List<Bank> list() {
        return StreamSupport.stream(bankRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Bank get(String id) throws BankNotFoundException {
        return bankRepository.findById(id)
                .orElseThrow(() -> new BankNotFoundException(id));
    }

    @Transactional
    @Override
    public BankBuilder create() {
        return serviceLocator.create(InitializableBankBuilder.class)
                .initializeNew();
    }

    @Transactional
    @Override
    public BankBuilder update(String id) throws BankNotFoundException {
        Bank existingBank = get(id);
        return serviceLocator.create(InitializableBankBuilder.class)
                .initializeExisting(existingBank);
    }

}
