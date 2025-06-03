package com.swunitzel.fiterview.controller;

import com.swunitzel.fiterview.apiPayload.ApiResponse;
import com.swunitzel.fiterview.dto.SchoolRecordResponseDto;
import com.swunitzel.fiterview.jwt.CustomUserDetails;
import com.swunitzel.fiterview.services.SchoolRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/school-records")
public class SchoolRecordController {

    private final SchoolRecordService schoolRecordService;

    @GetMapping("/analyze")
    public ApiResponse<SchoolRecordResponseDto.SchoolRecordDto> getSchoolRecord(@AuthenticationPrincipal CustomUserDetails userDetails) {
        SchoolRecordResponseDto.SchoolRecordDto schoolRecordDto = schoolRecordService.getSchoolRecord(userDetails.getUsername());
        return ApiResponse.onSuccess(schoolRecordDto);
    }
}
