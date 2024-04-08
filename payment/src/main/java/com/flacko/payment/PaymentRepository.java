package com.flacko.payment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends CrudRepository<PaymentPojo, Long> {

    Optional<Payment> findById(String id);

    List<Payment> listByTraderId(String traderId);

    List<Payment> listByMerchantId(String merchantId);

    List<Payment> listByCardId(String cardId);

}
