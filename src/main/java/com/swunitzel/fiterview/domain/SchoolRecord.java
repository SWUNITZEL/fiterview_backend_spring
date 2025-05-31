package com.swunitzel.fiterview.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

@Document(collection = "documents")
@Getter
@Setter
@NoArgsConstructor
public class SchoolRecord {
    @Id
    private String id;

    @Field("user_email")
    private String userEmail;

    private String content;

    private Map<String, List<Integer>>  grades;

    private String features;

    private String type;

    private List<String> hashtags;

    private String explanation;

}