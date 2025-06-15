package com.swunitzel.fiterview.controller;

import com.swunitzel.fiterview.apiPayload.ApiResponse;
import com.swunitzel.fiterview.dto.ReportDto;
import com.swunitzel.fiterview.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/{interviewId}/nonverbal-communication")
    public ApiResponse<ReportDto.NonverbalCommunicationReportDto> getNonVerbalCommunicationReport(@PathVariable(name = "interviewId") String interviewId) {
        ReportDto.NonverbalCommunicationReportDto reportDto = reportService.getNonverbalCommunicationReport(interviewId);
        return ApiResponse.onSuccess(reportDto);
    }

    @PostMapping("/{interviewId}/transmission")
    public ApiResponse<ReportDto.TransmissionReportDto> getTransmissionReport(@PathVariable(name = "interviewId") String interviewId) {
        ReportDto.TransmissionReportDto reportDto = reportService.getTransmissionReport(interviewId);
        return ApiResponse.onSuccess(reportDto);
    }
}
