package com.flacko.payment.verification.sms.impl;

import com.flacko.payment.verification.sms.service.SmsPaymentVerification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SmsPaymentVerificationRepository extends CrudRepository<SmsPaymentVerificationPojo, Long>,
        JpaSpecificationExecutor<SmsPaymentVerification> {

    @Query("SELECT s FROM SmsPaymentVerificationPojo s WHERE s.id = :id")
    Optional<SmsPaymentVerification> findById(String id);

    @Query("SELECT s FROM SmsPaymentVerificationPojo s WHERE s.paymentId = :paymentId")
    Optional<SmsPaymentVerification> findByPaymentId(String paymentId);

}
