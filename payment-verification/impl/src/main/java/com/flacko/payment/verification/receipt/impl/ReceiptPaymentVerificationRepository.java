package com.flacko.payment.verification.receipt.impl;

import com.flacko.payment.verification.receipt.service.ReceiptPaymentVerification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReceiptPaymentVerificationRepository extends CrudRepository<ReceiptPaymentVerificationPojo, Long>,
        JpaSpecificationExecutor<ReceiptPaymentVerification> {

    @Query("SELECT r FROM ReceiptPaymentVerificationPojo r WHERE r.id = :id")
    Optional<ReceiptPaymentVerification> findById(String id);

}
