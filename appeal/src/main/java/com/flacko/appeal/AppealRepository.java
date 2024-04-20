package com.flacko.appeal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppealRepository extends CrudRepository<AppealPojo, Long> {
}
