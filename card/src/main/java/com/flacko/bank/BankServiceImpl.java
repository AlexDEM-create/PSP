package com.flacko.bank;

import com.flacko.bank.exception.BankNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
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
