package com.flacko.payment;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends CrudRepository<PaymentPojo, Long> {

    @Query("SELECT p FROM payments p WHERE p.id = ?1")
    Optional<Payment> findById(String id);

    @Query("SELECT p FROM payments p WHERE p.traderTeamId = ?1")
    List<Payment> listByTraderTeamId(String traderTeamId);

    @Query("SELECT p FROM payments p WHERE p.merchantId = ?1")
    List<Payment> listByMerchantId(String merchantId);

    @Query("SELECT p FROM payments p WHERE p.cardId = ?1")
    List<Payment> listByCardId(String cardId);

}
