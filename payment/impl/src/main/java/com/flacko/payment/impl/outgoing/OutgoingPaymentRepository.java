package com.flacko.payment.impl.outgoing;

import com.flacko.payment.service.outgoing.OutgoingPayment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OutgoingPaymentRepository extends CrudRepository<OutgoingPaymentPojo, Long>,
        JpaSpecificationExecutor<OutgoingPayment> {

    @Query("SELECT p FROM OutgoingPaymentPojo p WHERE p.id = :id")
    Optional<OutgoingPayment> findById(String id);

}
