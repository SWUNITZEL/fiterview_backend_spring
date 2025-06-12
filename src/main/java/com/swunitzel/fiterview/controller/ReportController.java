package com.swunitzel.fiterview.controller;

import com.swunitzel.fiterview.apiPayload.ApiResponse;
import com.swunitzel.fiterview.dto.ReportDto;
import com.swunitzel.fiterview.dto.SchoolRecordResponseDto;
import com.swunitzel.fiterview.jwt.CustomUserDetails;
import com.swunitzel.fiterview.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/{interviewId}/nonverbal-communication")
    public ApiResponse<ReportDto.NonverbalCommunicationReportDto> getNonVerbalCommunicationReport(@PathVariable(name = "interviewId") String interviewId) {
        ReportDto.NonverbalCommunicationReportDto reportDto = reportService.getNonverbalCommunicationReport(interviewId);
        return ApiResponse.onSuccess(reportDto);
    }
}
