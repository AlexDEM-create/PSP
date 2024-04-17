package com.flacko.payment.verification.sms;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SmsPaymentVerificationRepository extends CrudRepository<SmsPaymentVerificationPojo, Long> {

    Optional<SmsPaymentVerification> findById(String id);

    Optional<SmsPaymentVerification> findByPaymentId(String paymentId);

    List<SmsPaymentVerification> listByTraderId(String traderId);

    List<SmsPaymentVerification> listByMerchantId(String merchantId);

    List<SmsPaymentVerification> listByCardId(String cardId);

}
