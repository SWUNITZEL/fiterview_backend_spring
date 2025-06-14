package com.swunitzel.fiterview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ReportDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NonverbalCommunicationReportTotalScoreDto{
        private float avgPostureScore;
        private float avgFacialScore;
        private float avgGazeScore;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NonverbalCommunicationReportDto{
        private NonverbalCommunicationReportTotalScoreDto totalScore;
        private float avgShoulderTiltCount;
        private float avgTurnLeftCount;
        private float avgTurnRightCount;
        private List<List<Integer>> gazePointList;

    }
}
