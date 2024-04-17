package com.flacko.auth.security.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserPojo, Long> {

    @Query("SELECT u FROM users u WHERE u.login = ?1")
    Optional<User> findByLogin(String login);

    @Query("SELECT u FROM users u WHERE u.id = ?1")
    Optional<User> findById(String id);

}
