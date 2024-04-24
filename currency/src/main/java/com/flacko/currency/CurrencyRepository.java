package com.flacko.currency;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends CrudRepository<CurrencyPojo, Long> {

    @Query("SELECT c FROM CurrencyPojo c WHERE c.id = :id")
    Optional<Currency> findById(String id);

}
