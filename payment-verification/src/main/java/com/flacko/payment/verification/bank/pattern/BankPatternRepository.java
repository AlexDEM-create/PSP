package com.flacko.payment.verification.bank.pattern;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankPatternRepository extends CrudRepository<BankPatternPojo, Long> {

    @Query("SELECT bp FROM bank_patterns bp WHERE bp.id = ?1")
    Optional<BankPattern> findById(String id);

    @Query("SELECT bp FROM bank_patterns bp WHERE bp.bankId = ?1")
    List<BankPattern> findByBankId(String bankId);

    @Query("SELECT bp FROM bank_patterns bp WHERE bp.type = ?1")
    List<BankPattern> findByType(BankPatternType type);

}
