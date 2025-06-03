package com.swunitzel.fiterview.services;

import com.swunitzel.fiterview.apiPayload.code.status.ErrorStatus;
import com.swunitzel.fiterview.apiPayload.exception.handler.SchoolRecordHandler;
import com.swunitzel.fiterview.converter.SchoolRecordConverter;
import com.swunitzel.fiterview.domain.SchoolRecord;
import com.swunitzel.fiterview.dto.SchoolRecordResponseDto;
import com.swunitzel.fiterview.repository.SchoolRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SchoolRecordService {
    private final SchoolRecordRepository schoolRecordRepository;

    public String getSchoolRecordId(String user_email) {

        Optional<SchoolRecord> schoolRecord = schoolRecordRepository.findByUserEmail(user_email);

        if (schoolRecord.isEmpty()) {
            return null;
        }

        return schoolRecord.get().getId();
    }

    public SchoolRecord findSchoolRecord(String user_email) {
        return schoolRecordRepository.findByUserEmail(user_email)
                .orElseThrow(() -> new SchoolRecordHandler(ErrorStatus._SCHOOL_RECORD_NOT_FOUND));
    }


    public SchoolRecordResponseDto.SchoolRecordDto getSchoolRecord(String user_email) {
        SchoolRecord schoolRecord = findSchoolRecord(user_email);

        return SchoolRecordConverter.toDto(schoolRecord);
    }
}
