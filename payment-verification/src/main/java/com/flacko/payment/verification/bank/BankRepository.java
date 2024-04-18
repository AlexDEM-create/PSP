package com.flacko.payment.verification.bank;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankRepository extends CrudRepository<BankPojo, Long> {

    Optional<Bank> findById(String id);

    Optional<Bank> findByName(String name);

}
