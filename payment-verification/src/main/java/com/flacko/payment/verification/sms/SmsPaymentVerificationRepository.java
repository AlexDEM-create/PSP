package com.flacko.payment.verification.sms;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SmsPaymentVerificationRepository extends CrudRepository<SmsPaymentVerificationPojo, Long> {

    @Query("SELECT s FROM sms_payment_verifications s WHERE s.id = ?1")
    Optional<SmsPaymentVerification> findById(String id);

    @Query("SELECT s FROM sms_payment_verifications s WHERE s.paymentId = ?1")
    Optional<SmsPaymentVerification> findByPaymentId(String paymentId);

}
