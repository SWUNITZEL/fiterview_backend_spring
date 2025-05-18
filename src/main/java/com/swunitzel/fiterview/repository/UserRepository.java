package com.swunitzel.fiterview.repository;

import com.swunitzel.fiterview.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    boolean existsByEmail(String email);

    User findByEmail(String email);
}
