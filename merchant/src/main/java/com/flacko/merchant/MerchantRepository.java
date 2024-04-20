package com.flacko.merchant;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends CrudRepository<MerchantPojo, Long> {

    @Query("SELECT m FROM MerchantPojo m WHERE m.id = :id")
    Optional<Merchant> findById(String id);

}
