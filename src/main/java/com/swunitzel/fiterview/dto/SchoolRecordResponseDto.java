package com.swunitzel.fiterview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

public class SchoolRecordResponseDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SchoolRecordDto{

        Map<String, List<Integer>> grades;

        private String type;

        private List<String> hashtags;

        private String explanation;
    }
}
