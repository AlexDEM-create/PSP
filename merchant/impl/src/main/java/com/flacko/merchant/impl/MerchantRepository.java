package com.flacko.merchant.impl;

import com.flacko.merchant.service.Merchant;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends CrudRepository<MerchantPojo, Long>, JpaSpecificationExecutor<Merchant> {

    @Query("SELECT m FROM MerchantPojo m WHERE m.id = :id")
    Optional<Merchant> findById(String id);

    @Query("SELECT m FROM MerchantPojo m WHERE m.userId = :userId")
    Optional<Merchant> findByUserId(String userId);

}
