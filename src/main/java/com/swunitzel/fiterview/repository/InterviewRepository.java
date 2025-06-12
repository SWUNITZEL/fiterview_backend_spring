package com.swunitzel.fiterview.repository;

import com.swunitzel.fiterview.domain.Interview;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InterviewRepository extends MongoRepository<Interview, String> {
}
