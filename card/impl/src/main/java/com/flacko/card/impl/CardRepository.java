package com.flacko.card.impl;

import com.flacko.card.service.Card;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends CrudRepository<CardPojo, Long> {

    @Query("SELECT c FROM CardPojo c WHERE c.id = :id")
    Optional<Card> findById(String id);

    @Query("SELECT c FROM CardPojo c WHERE c.traderTeamId = :traderTeamId")
    List<Card> listByTraderTeamId(String traderTeamId);

}
