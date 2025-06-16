package com.swunitzel.fiterview.repository;

import com.swunitzel.fiterview.domain.Combine;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CombineRepository extends MongoRepository<Combine, String> {
}
