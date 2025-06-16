package com.swunitzel.fiterview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
        private List<List<List<Integer>>> gazePointList;
        private LocalDateTime createdAt;
        private String university;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransmissionReportTotalScoreDto{
        private float avgHesitantScore;
        private float avgPitchScore;
        private float avgSpeedScore;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransmissionReportDto{
        private TransmissionReportTotalScoreDto totalScore;
        private List<List<Object>> frequentlyUsedWords;
        private List<List<String>> hesitantList;
        private LocalDateTime createdAt;
        private String university;

    }
}
