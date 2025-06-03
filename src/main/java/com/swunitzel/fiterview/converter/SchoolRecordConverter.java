package com.swunitzel.fiterview.converter;

import com.swunitzel.fiterview.domain.SchoolRecord;
import com.swunitzel.fiterview.dto.SchoolRecordResponseDto;

public class SchoolRecordConverter {

    public static SchoolRecordResponseDto.SchoolRecordDto toDto(SchoolRecord schoolRecord) {
        return SchoolRecordResponseDto.SchoolRecordDto.builder()
                .grades(schoolRecord.getGrades())
                .explanation(schoolRecord.getExplanation())
                .hashtags(schoolRecord.getHashtags())
                .type(schoolRecord.getType())
                .build();
    }
}
