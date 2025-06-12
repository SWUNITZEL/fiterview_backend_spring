package com.swunitzel.fiterview.services;

import com.swunitzel.fiterview.apiPayload.code.status.ErrorStatus;
import com.swunitzel.fiterview.apiPayload.exception.handler.InterviewHandler;
import com.swunitzel.fiterview.converter.ReportConverter;
import com.swunitzel.fiterview.domain.Answer;
import com.swunitzel.fiterview.domain.Interview;
import com.swunitzel.fiterview.dto.ReportDto;
import com.swunitzel.fiterview.repository.AnswerRepository;
import com.swunitzel.fiterview.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final AnswerRepository answerRepository;
    private final InterviewRepository interviewRepository;

    public ReportDto.NonverbalCommunicationReportDto getNonverbalCommunicationReport(String interviewId) {

        List<Answer> answers = answerRepository.findAllByInterviewId(interviewId);

        // 영상 분석 결과 지표 합계
        float totalPostureScore = 0f;
        float totalSmileRatio = 0f;
        int totalGazeDownScore = 0;

        // 자세 측정 카운드
        int totalShoulderTiltCount = 0;
        int totalTurnLeftCount = 0;
        int totalTurnRightCount = 0;

        for (Answer answer : answers) {
            totalPostureScore += scorePosture(
                    answer.getShoulderTiltCount(),
                    answer.getTurnLeftCount(),
                    answer.getTurnRightCount());
            totalSmileRatio += answer.getSmileRatio();
            totalGazeDownScore += scoreGaze(answer.getGazeDownCount());
            totalShoulderTiltCount += answer.getShoulderTiltCount();
            totalTurnLeftCount += answer.getTurnLeftCount();
            totalTurnRightCount += answer.getTurnRightCount();
        }

        int answerCount = answers.size();

        float avgPostureScore = totalPostureScore / answerCount;
        float avgFacialScore = ( totalSmileRatio / answerCount ) * 100;
        float avgGazeScore = totalGazeDownScore / answerCount;
        float avgShoulderTiltCount = totalShoulderTiltCount / answerCount;
        float avgTurnLeftCount = totalTurnLeftCount / answerCount;
        float avgTurnRightCount = totalTurnRightCount / answerCount;

        // 인터뷰에 총첨 업데이트
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new InterviewHandler(ErrorStatus._INTERVIEW_NOT_FOUND));

        Interview updatedInterview = interview.updateTotalScore(avgPostureScore, avgFacialScore, avgGazeScore,
                avgShoulderTiltCount, avgTurnLeftCount, avgTurnRightCount);

        return ReportConverter.toDto(updatedInterview);
    }

    int scoreGaze(int downCount) {
        if (downCount <= 3) {
            return 100;
        } else if (downCount <= 6) {
            return 90;
        } else if (downCount <= 10) {
            return 80;
        } else if (downCount <= 15) {
            return 70;
        } else {
            return 60;
        }
    }

    float scorePosture(int shoulderTilt, int turnLeft, int turnRight) {

        // 가중치 적용 (어깨기울기 2배, 고개 돌림 각각 1배)
        int weightedSum = shoulderTilt * 2 + turnLeft + turnRight;

        return 100f - weightedSum * 1.5f;
    }
}