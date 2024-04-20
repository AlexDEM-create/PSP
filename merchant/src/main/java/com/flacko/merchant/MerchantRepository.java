package com.flacko.merchant;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends CrudRepository<MerchantPojo, Long> {

    @Query("SELECT m FROM merchants m WHERE m.id = ?1")
    Optional<Merchant> findById(String id);

}
