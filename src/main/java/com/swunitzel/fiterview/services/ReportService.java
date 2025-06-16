package com.swunitzel.fiterview.services;

import com.swunitzel.fiterview.apiPayload.code.status.ErrorStatus;
import com.swunitzel.fiterview.apiPayload.exception.handler.AnswerHandler;
import com.swunitzel.fiterview.apiPayload.exception.handler.CombineHandler;
import com.swunitzel.fiterview.apiPayload.exception.handler.InterviewHandler;
import com.swunitzel.fiterview.converter.ReportConverter;
import com.swunitzel.fiterview.domain.Answer;
import com.swunitzel.fiterview.domain.Combine;
import com.swunitzel.fiterview.domain.Interview;
import com.swunitzel.fiterview.dto.ReportDto;
import com.swunitzel.fiterview.repository.AnswerRepository;
import com.swunitzel.fiterview.repository.CombineRepository;
import com.swunitzel.fiterview.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final AnswerRepository answerRepository;
    private final InterviewRepository interviewRepository;
    private final CombineRepository combineRepository;

    // 비언어적 커뮤니케이션 분석 결과 보고서
    @Transactional
    public ReportDto.NonverbalCommunicationReportDto getNonverbalCommunicationReport(String interviewId) {

        List<Answer> answers = answerRepository.findAllByInterviewId(interviewId);

        if (answers == null) {
            throw new AnswerHandler(ErrorStatus._ANSWER_NOT_FOUND);
        }

        // 영상 분석 결과 지표 합계
        float totalPostureScore = 0f;
        float totalSmileScore = 0f;
        int totalGazeScore = 0;

        // 자세 측정 카운드
        int totalShoulderTiltCount = 0;
        int totalTurnLeftCount = 0;
        int totalTurnRightCount = 0;

        for (Answer answer : answers) {
            totalPostureScore += scorePosture(
                    answer.getShoulderTiltCount(),
                    answer.getTurnLeftCount(),
                    answer.getTurnRightCount());
            totalSmileScore += scoreSmile(answer.getSmileRatio());
            totalGazeScore += scoreGaze(answer.getGazeDownCount(), answer.getBlinksPerMinute());
            totalShoulderTiltCount += answer.getShoulderTiltCount();
            totalTurnLeftCount += answer.getTurnLeftCount();
            totalTurnRightCount += answer.getTurnRightCount();
        }
        int answerCount = answers.size();

        float avgPostureScore = totalPostureScore / answerCount;
        float avgFacialScore = totalSmileScore / answerCount;
        float avgGazeScore = totalGazeScore / answerCount;
        float avgShoulderTiltCount = totalShoulderTiltCount / answerCount;
        float avgTurnLeftCount = totalTurnLeftCount / answerCount;
        float avgTurnRightCount = totalTurnRightCount / answerCount;

        List<List<List<Integer>>> gazePointsList = answers.stream()
                .map(Answer::getGazePoints)
                .collect(Collectors.toList());

        // 인터뷰에 총첨 업데이트
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new InterviewHandler(ErrorStatus._INTERVIEW_NOT_FOUND));

        Interview updatedInterview = interview.updateNonverbalCommunicationReport(avgPostureScore, avgFacialScore, avgGazeScore,
                avgShoulderTiltCount, avgTurnLeftCount, avgTurnRightCount, gazePointsList);

        Combine combine = combineRepository.findById(interview.getCombineId())
                .orElseThrow(() -> new CombineHandler(ErrorStatus._COMBINE_NOT_FOUND));

        return ReportConverter.toNonverbalCommunicationReportDto(updatedInterview, combine);
    }

    float scoreGaze(int downCount, float blinksPerMinute) {
        int gazeScore;

        if (downCount <= 3) {
            gazeScore = 100;
        } else if (downCount <= 6) {
            gazeScore = 90;
        } else if (downCount <= 10) {
            gazeScore = 80;
        } else if (downCount <= 15) {
            gazeScore = 70;
        } else {
            gazeScore = 60;
        }

        float penalty = 0;
        if (blinksPerMinute < 10) {
            penalty = (10 - blinksPerMinute) * 2;  // 너무 적은 경우 감점
        } else if (blinksPerMinute > 20) {
            penalty = (blinksPerMinute - 20) * 2;  // 너무 많은 경우 감점
        }

        return Math.max(0, gazeScore - penalty);
    }

    float scorePosture(int shoulderTilt, int turnLeft, int turnRight) {

        // 가중치 적용 (어깨기울기 2배, 고개 돌림 각각 1배)
        int weightedSum = shoulderTilt * 2 + turnLeft + turnRight;

        return 100f - weightedSum * 1.5f;
    }

    float scoreSmile(float smilRatio) {
        if (smilRatio >= 0.5f) {
            return 90 + (smilRatio - 0.5f) * 20;  // 90~100점
        } else if (smilRatio >= 0.3f) {
            return 70 + (smilRatio - 0.3f) * 100; // 70~89점
        } else {
            return 50 * smilRatio / 0.3f;         // 0~50점
        }
    }

    // 어휘 분석 결과
    @Transactional
    public ReportDto.TransmissionReportDto getTransmissionReport(String interviewId) {

        List<Answer> answers = answerRepository.findAllByInterviewId(interviewId);

        if (answers == null) {
            throw new AnswerHandler(ErrorStatus._ANSWER_NOT_FOUND);
        }

        // 어휘 분석 결과 점수 합계
        float totalHesitantScore = 0f;
        float totalPitchScore = 0f;
        int totalSpeedScore = 0;

        for (Answer answer : answers) {
            totalHesitantScore += answer.getHesitantScore();
            totalPitchScore += getPitchScore(answer.getPitchMean());
            totalSpeedScore += getSpeedScore(answer.getSpeakingSpeed());
        }

        int answerCount = answers.size();

        float avgHesitantScore = totalHesitantScore / answerCount;
        float avgPitchScore = totalPitchScore / answerCount;
        float avgSpeedScore = totalSpeedScore / answerCount;
        
        List<List<Object>> frequentlyUsedWords = answers.stream()
                .map(Answer::getFrequentlyUsedWords)
                .collect(Collectors.toList());

        List<List<String>> hesitantList = answers.stream()
                .map(Answer::getHesitantList)
                .collect(Collectors.toList());

        // 인터뷰에 총첨 업데이트
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new InterviewHandler(ErrorStatus._INTERVIEW_NOT_FOUND));

        Interview updatedInterview = interview.updateTransmissionReport(avgHesitantScore, avgPitchScore, avgSpeedScore,
                frequentlyUsedWords, hesitantList);

        Combine combine = combineRepository.findById(interview.getCombineId())
                .orElseThrow(() -> new CombineHandler(ErrorStatus._COMBINE_NOT_FOUND));

        return ReportConverter.toTransmissionReportDto(updatedInterview, combine);
    }

    // 톤 점수 계산
    public static int getPitchScore(float pitchMean) {
        if (pitchMean >= 85 && pitchMean <= 180) {
            return 100;
        } else if ((pitchMean >= 70 && pitchMean < 85) || (pitchMean > 180 && pitchMean <= 210)) {
            return 80;
        } else {
            return 60;
        }
    }

    // 속도 점수 계산
    public static int getSpeedScore(float speakingSpeed) {
        if (speakingSpeed >= 3.0 && speakingSpeed <= 4.5) {
            return 100;
        } else if ((speakingSpeed >= 2.0 && speakingSpeed < 3.0) || (speakingSpeed > 4.5 && speakingSpeed <= 6.0)) {
            return 80;
        } else {
            return 60;
        }
    }

}