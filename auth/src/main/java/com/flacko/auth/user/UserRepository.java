package com.flacko.auth.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserPojo, Long> {

    Optional<User> findByLogin(String login);

    Optional<User> findById(String id);

}
