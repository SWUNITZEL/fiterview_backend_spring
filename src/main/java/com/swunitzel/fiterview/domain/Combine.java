package com.swunitzel.fiterview.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "combine")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Combine {

    @Id
    private String id;

    @Field("university")
    private String university;
}
