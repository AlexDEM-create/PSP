package com.flacko.bank.pattern.impl;

import com.flacko.bank.pattern.service.BankPattern;
import com.flacko.bank.pattern.service.BankPatternType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankPatternRepository extends CrudRepository<BankPatternPojo, Long> {

    @Query("SELECT bp FROM BankPatternPojo bp WHERE bp.id = :id")
    Optional<BankPattern> findById(String id);

    @Query("SELECT bp FROM BankPatternPojo bp WHERE bp.bankId = :bankId")
    List<BankPattern> listByBankId(String bankId);

    @Query("SELECT bp FROM BankPatternPojo bp WHERE bp.type = :type")
    List<BankPattern> findByType(BankPatternType type);

}
