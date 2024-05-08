package com.flacko.payment.method.impl;

import com.flacko.payment.method.service.PaymentMethod;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends CrudRepository<PaymentMethodPojo, Long>,
        JpaSpecificationExecutor<PaymentMethod> {

    @Query("SELECT c FROM PaymentMethodPojo c WHERE c.id = :id")
    Optional<PaymentMethod> findById(String id);

}
