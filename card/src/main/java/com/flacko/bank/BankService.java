package com.flacko.bank;

import com.flacko.bank.exception.BankNotFoundException;

import java.util.List;

public interface BankService {

    List<Bank> list();

    Bank get(String id) throws BankNotFoundException;

}
