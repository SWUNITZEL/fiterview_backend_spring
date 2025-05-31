package com.swunitzel.fiterview.services;

import com.swunitzel.fiterview.domain.SchoolRecord;
import com.swunitzel.fiterview.repository.SchoolRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchoolRecordService {
    private final SchoolRecordRepository schoolRecordRepository;

    public String getSchoolRecordId(String user_email) {

        SchoolRecord schoolRecord = schoolRecordRepository.findByUserEmail(user_email);

        if (schoolRecord == null) {
            throw new IllegalArgumentException("생활기록부가 존재하지 않음" );
        }

        return schoolRecord.getId();
    }

}
