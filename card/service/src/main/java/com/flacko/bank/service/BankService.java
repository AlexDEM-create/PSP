package com.flacko.bank.service;

import com.flacko.common.exception.BankNotFoundException;

import java.util.List;

public interface BankService {

    List<Bank> list();

    Bank get(String id) throws BankNotFoundException;

}
