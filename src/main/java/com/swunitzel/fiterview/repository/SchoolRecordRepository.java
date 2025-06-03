package com.swunitzel.fiterview.repository;

import com.swunitzel.fiterview.domain.SchoolRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SchoolRecordRepository extends MongoRepository<SchoolRecord, String> {
    Optional<SchoolRecord> findByUserEmail(String user_email);
}