package com.flacko.appeal.impl;

import com.flacko.appeal.service.Appeal;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppealRepository extends CrudRepository<AppealPojo, Long>, JpaSpecificationExecutor<Appeal> {

    @Query("SELECT a FROM AppealPojo a WHERE a.id = :id")
    Optional<Appeal> findById(String id);

}
