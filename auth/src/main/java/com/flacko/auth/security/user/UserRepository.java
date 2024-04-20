package com.flacko.auth.security.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserPojo, Long> {

    @Query("SELECT u FROM UserPojo u WHERE u.login = :login")
    Optional<User> findByLogin(String login);

    @Query("SELECT u FROM UserPojo u WHERE u.id = :id")
    Optional<User> findById(String id);

}
