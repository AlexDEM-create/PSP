package com.flacko.payment.verification.receipt;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReceiptPaymentVerificationRepository extends CrudRepository<ReceiptPaymentVerificationPojo, Long> {

    @Query("SELECT r FROM receipt_payment_verifications r WHERE r.id = ?1")
    Optional<ReceiptPaymentVerification> findById(String id);

    @Query("SELECT r FROM receipt_payment_verifications r WHERE r.paymentId = ?1")
    Optional<ReceiptPaymentVerification> findByPaymentId(String paymentId);

}
