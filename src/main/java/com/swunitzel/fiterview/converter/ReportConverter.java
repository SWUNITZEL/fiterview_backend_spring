package com.swunitzel.fiterview.converter;

import com.swunitzel.fiterview.domain.Interview;
import com.swunitzel.fiterview.dto.ReportDto;

public class ReportConverter {

    public static ReportDto.NonverbalCommunicationReportDto toDto(Interview interview) {
        return ReportDto.NonverbalCommunicationReportDto.builder()
                .totalScore(toTotalScoreDto(interview))
                .avgShoulderTiltCount(interview.getAvgShoulderTiltCount())
                .avgTurnLeftCount(interview.getAvgTurnLeftCount())
                .avgTurnRightCount(interview.getAvgTurnRightCount())
                .gazePointList(interview.getGazePointsList())
                .build();
    }

    public static ReportDto.NonverbalCommunicationReportTotalScoreDto toTotalScoreDto(Interview interview) {
        return ReportDto.NonverbalCommunicationReportTotalScoreDto.builder()
                .avgPostureScore(interview.getAvgPostureScore())
                .avgFacialScore(interview.getAvgFacialScore())
                .avgGazeScore(interview.getAvgGazeScore())
                .build();
    }
}
