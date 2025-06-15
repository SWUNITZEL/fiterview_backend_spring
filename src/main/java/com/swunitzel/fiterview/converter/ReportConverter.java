package com.swunitzel.fiterview.converter;

import com.swunitzel.fiterview.domain.Interview;
import com.swunitzel.fiterview.dto.ReportDto;

public class ReportConverter {

    public static ReportDto.NonverbalCommunicationReportDto toNonverbalCommunicationReportDto(Interview interview) {
        return ReportDto.NonverbalCommunicationReportDto.builder()
                .totalScore(toNonverbalCommunicationReportTotalScoreDto(interview))
                .avgShoulderTiltCount(interview.getAvgShoulderTiltCount())
                .avgTurnLeftCount(interview.getAvgTurnLeftCount())
                .avgTurnRightCount(interview.getAvgTurnRightCount())
                .gazePointList(interview.getGazePointsList())
                .build();
    }

    public static ReportDto.NonverbalCommunicationReportTotalScoreDto toNonverbalCommunicationReportTotalScoreDto(Interview interview) {
        return ReportDto.NonverbalCommunicationReportTotalScoreDto.builder()
                .avgPostureScore(interview.getAvgPostureScore())
                .avgFacialScore(interview.getAvgFacialScore())
                .avgGazeScore(interview.getAvgGazeScore())
                .build();
    }

    public static ReportDto.TransmissionReportDto toTransmissionReportDto(Interview interview) {
        return ReportDto.TransmissionReportDto.builder()
                .totalScore(toTransmissionReportTotalScoreDto(interview))
                .frequentlyUsedWords(interview.getFrequentlyUsedWords())
                .hesitantList(interview.getHesitantList())
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
