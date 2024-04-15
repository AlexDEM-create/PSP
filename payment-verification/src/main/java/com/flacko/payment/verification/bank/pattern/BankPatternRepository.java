package com.flacko.payment.verification.bank.pattern;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankPatternRepository extends CrudRepository<BankPatternPojo, Long> {

    Optional<BankPattern> findById(String id);

    List<BankPattern> findByBankId(String bankId);

    List<BankPattern> findByType(BankPatternType type);

}
