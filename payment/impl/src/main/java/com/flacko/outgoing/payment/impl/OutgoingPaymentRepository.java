package com.flacko.outgoing.payment.impl;

import com.flacko.payment.service.Payment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OutgoingPaymentRepository extends CrudRepository<OutgoingPaymentPojo, Long>, JpaSpecificationExecutor<Payment> {

    @Query("SELECT p FROM IncomingPaymentPojo p WHERE p.id = :id")
    Optional<Payment> findById(String id);

    @Query("SELECT p FROM IncomingPaymentPojo p WHERE p.traderTeamId = :traderTeamId")
    List<Payment> listByTraderTeamId(String traderTeamId);

    @Query("SELECT p FROM IncomingPaymentPojo p WHERE p.merchantId = :merchantId")
    List<Payment> listByMerchantId(String merchantId);

    @Query("SELECT p FROM IncomingPaymentPojo p WHERE p.cardId = :cardId")
    List<Payment> listByCardId(String cardId);

}
