package com.flacko.bank.impl;

import com.flacko.bank.service.Bank;
import com.flacko.bank.service.BankService;
import com.flacko.common.exception.BankNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;

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

}
