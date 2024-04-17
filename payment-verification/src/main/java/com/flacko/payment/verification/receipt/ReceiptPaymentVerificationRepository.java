package com.flacko.payment.verification.receipt;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceiptPaymentVerificationRepository extends CrudRepository<ReceiptPaymentVerificationPojo, Long> {

    Optional<ReceiptPaymentVerification> findById(String id);

    Optional<ReceiptPaymentVerification> findByPaymentId(String paymentId);

    List<ReceiptPaymentVerification> listByTraderId(String traderId);

    List<ReceiptPaymentVerification> listByMerchantId(String merchantId);

    List<ReceiptPaymentVerification> listByCardId(String cardId);

}
