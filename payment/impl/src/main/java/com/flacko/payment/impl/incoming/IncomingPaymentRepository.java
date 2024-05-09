package com.flacko.payment.impl.incoming;

import com.flacko.payment.service.incoming.IncomingPayment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IncomingPaymentRepository extends CrudRepository<IncomingPaymentPojo, Long>,
        JpaSpecificationExecutor<IncomingPayment> {

    @Query("SELECT p FROM IncomingPaymentPojo p WHERE p.id = :id")
    Optional<IncomingPayment> findById(String id);

}
