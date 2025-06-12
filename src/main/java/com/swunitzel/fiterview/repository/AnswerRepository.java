package com.swunitzel.fiterview.repository;

import com.swunitzel.fiterview.domain.Answer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AnswerRepository extends MongoRepository<Answer, String> {

    List<Answer> findAllByInterviewId(String interviewId);
}
