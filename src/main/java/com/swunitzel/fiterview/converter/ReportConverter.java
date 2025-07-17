package com.swunitzel.fiterview.converter;

import com.swunitzel.fiterview.domain.Combine;
import com.swunitzel.fiterview.domain.GazePoint;
import com.swunitzel.fiterview.domain.Interview;
import com.swunitzel.fiterview.dto.ReportDto;

import java.util.List;
import java.util.stream.Collectors;

public class ReportConverter {

    public static ReportDto.GazePointsDto toGazePointDto(GazePoint gazePoint) {
        return ReportDto.GazePointsDto.builder()
                .x(gazePoint.getX())
                .y(gazePoint.getY())
                .time(gazePoint.getTime())
                .build();
    }

    public static ReportDto.NonverbalCommunicationReportDto toNonverbalCommunicationReportDto(Interview interview, Combine combine) {
        List<List<ReportDto.GazePointsDto>> gazePointsList = interview.getGazePointsList().stream()
                .map(innerList -> innerList.stream()
                        .map(ReportConverter::toGazePointDto)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        return ReportDto.NonverbalCommunicationReportDto.builder()
                .totalScore(toNonverbalCommunicationReportTotalScoreDto(interview))
                .avgShoulderTiltCount(interview.getAvgShoulderTiltCount())
                .avgTurnLeftCount(interview.getAvgTurnLeftCount())
                .avgTurnRightCount(interview.getAvgTurnRightCount())
                .gazePointList(gazePointsList)
                .createdAt(interview.getCreatedAt())
                .university(combine.getUniversity())
                .build();
    }

    public static ReportDto.NonverbalCommunicationReportTotalScoreDto toNonverbalCommunicationReportTotalScoreDto(Interview interview) {
        return ReportDto.NonverbalCommunicationReportTotalScoreDto.builder()
                .avgPostureScore(interview.getAvgPostureScore())
                .avgFacialScore(interview.getAvgFacialScore())
                .avgGazeScore(interview.getAvgGazeScore())
                .build();
    }

    public static ReportDto.TransmissionReportDto toTransmissionReportDto(Interview interview, Combine combine) {
        return ReportDto.TransmissionReportDto.builder()
                .totalScore(toTransmissionReportTotalScoreDto(interview))
                .frequentlyUsedWords(interview.getFrequentlyUsedWords())
                .hesitantList(interview.getHesitantList())
                .createdAt(interview.getCreatedAt())
                .university(combine.getUniversity())
                .build();
    }

    public static ReportDto.TransmissionReportTotalScoreDto toTransmissionReportTotalScoreDto(Interview interview) {
        return ReportDto.TransmissionReportTotalScoreDto.builder()
                .avgHesitantScore(interview.getAvgHesitantScore())
                .avgPitchScore(interview.getAvgPitchScore())
                .avgSpeedScore(interview.getAvgSpeedScore())
                .build();
    }
}
