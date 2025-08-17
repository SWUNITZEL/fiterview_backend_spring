package com.swunitzel.fiterview.services;

import com.swunitzel.fiterview.apiPayload.exception.handler.AnswerHandler;
import com.swunitzel.fiterview.apiPayload.exception.handler.CombineHandler;
import com.swunitzel.fiterview.apiPayload.exception.handler.InterviewHandler;
import com.swunitzel.fiterview.converter.ReportConverter;
import com.swunitzel.fiterview.domain.Answer;
import com.swunitzel.fiterview.domain.Combine;
import com.swunitzel.fiterview.domain.GazePoint;
import com.swunitzel.fiterview.domain.Interview;
import com.swunitzel.fiterview.dto.ReportDto;
import com.swunitzel.fiterview.repository.AnswerRepository;
import com.swunitzel.fiterview.repository.CombineRepository;
import com.swunitzel.fiterview.repository.InterviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private InterviewRepository interviewRepository;

    @Mock
    private CombineRepository combineRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    @DisplayName("정상적인 비언어적 커뮤니케이션 리포트 생성 테스트")
    void getNonverbalCommunicationReport_Success() {
        // Given
        String interviewId = "test-interview-id";
        String combineId = "test-combine-id";

        // Mock Answer 데이터 생성
        Answer answer1 = createMockAnswer(5, 3, 2, 0.8f, 10, 15.0f);
        Answer answer2 = createMockAnswer(3, 4, 1, 0.6f, 8, 12.0f);
        List<Answer> answers = Arrays.asList(answer1, answer2);

        // Mock Interview 데이터 생성
        Interview interview = createMockInterview(interviewId, combineId);
        Interview updatedInterview = createMockUpdatedInterview();

        // Mock Combine 데이터 생성
        Combine combine = createMockCombine(combineId);

        // Mock ReportDto 생성
        ReportDto.NonverbalCommunicationReportDto expectedDto = createMockReportDto();

        // When
        when(answerRepository.findAllByInterviewId(interviewId)).thenReturn(answers);
        when(interviewRepository.findById(interviewId)).thenReturn(Optional.of(interview));
        when(interview.updateNonverbalCommunicationReport(anyFloat(), anyFloat(), anyFloat(),
                anyFloat(), anyFloat(), anyFloat(), anyList())).thenReturn(updatedInterview);
        when(combineRepository.findById(combineId)).thenReturn(Optional.of(combine));
        try (MockedStatic<ReportConverter> mocked = mockStatic(ReportConverter.class)) {
            mocked.when(() -> ReportConverter.toNonverbalCommunicationReportDto(updatedInterview, combine))
                    .thenReturn(expectedDto);

            // When
            ReportDto.NonverbalCommunicationReportDto result =
                    reportService.getNonverbalCommunicationReport(interviewId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(expectedDto);
        }

        // Verify 메서드 호출 검증
        verify(answerRepository).findAllByInterviewId(interviewId);
        verify(interviewRepository).findById(interviewId);
        verify(combineRepository).findById(combineId);
        verify(interview).updateNonverbalCommunicationReport(anyFloat(), anyFloat(), anyFloat(),
                anyFloat(), anyFloat(), anyFloat(), anyList());
    }

    @Test
    @DisplayName("Answer가 없을 경우 예외 발생 테스트")
    void getNonverbalCommunicationReport_AnswerNotFound() {
        // Given
        String interviewId = "test-interview-id";

        // When
        when(answerRepository.findAllByInterviewId(interviewId)).thenReturn(null);

        // Then
        assertThatThrownBy(() -> reportService.getNonverbalCommunicationReport(interviewId))
                .isInstanceOf(AnswerHandler.class);

        verify(answerRepository).findAllByInterviewId(interviewId);
    }

    @Test
    @DisplayName("Answer 필드가 null인 경우 예외 발생 테스트")
    void getNonverbalCommunicationReport_AnswerFieldIsNull() {
        // Given
        String interviewId = "test-interview-id";
        Answer answerWithNullFields = createMockAnswerWithNullFields();
        List<Answer> answers = Arrays.asList(answerWithNullFields);

        // When
        when(answerRepository.findAllByInterviewId(interviewId)).thenReturn(answers);

        // Then
        assertThatThrownBy(() -> reportService.getNonverbalCommunicationReport(interviewId))
                .isInstanceOf(AnswerHandler.class);

        verify(answerRepository).findAllByInterviewId(interviewId);
    }

    @Test
    @DisplayName("Interview가 없을 경우 예외 발생 테스트")
    void getNonverbalCommunicationReport_InterviewNotFound() {
        // Given
        String interviewId = "test-interview-id";
        Answer answer = createMockAnswer(5, 3, 2, 0.8f, 10, 15.0f);
        List<Answer> answers = Arrays.asList(answer);

        // When
        when(answerRepository.findAllByInterviewId(interviewId)).thenReturn(answers);
        when(interviewRepository.findById(interviewId)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> reportService.getNonverbalCommunicationReport(interviewId))
                .isInstanceOf(InterviewHandler.class);

        verify(answerRepository).findAllByInterviewId(interviewId);
        verify(interviewRepository).findById(interviewId);
    }

    @Test
    @DisplayName("Combine이 없을 경우 예외 발생 테스트")
    void getNonverbalCommunicationReport_CombineNotFound() {
        // Given
        String interviewId = "test-interview-id";
        String combineId = "test-combine-id";
        Answer answer = createMockAnswer(5, 3, 2, 0.8f, 10, 15.0f);
        List<Answer> answers = Arrays.asList(answer);
        Interview interview = createMockInterview(interviewId, combineId);
        Interview updatedInterview = createMockUpdatedInterview();

        // When
        when(answerRepository.findAllByInterviewId(interviewId)).thenReturn(answers);
        when(interviewRepository.findById(interviewId)).thenReturn(Optional.of(interview));
        when(interview.updateNonverbalCommunicationReport(anyFloat(), anyFloat(), anyFloat(),
                anyFloat(), anyFloat(), anyFloat(), anyList())).thenReturn(updatedInterview);
        when(combineRepository.findById(combineId)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> reportService.getNonverbalCommunicationReport(interviewId))
                .isInstanceOf(CombineHandler.class);

        verify(answerRepository).findAllByInterviewId(interviewId);
        verify(interviewRepository).findById(interviewId);
        verify(combineRepository).findById(combineId);
    }

    @Test
    @DisplayName("GazePoints가 null인 경우 빈 리스트로 처리되는지 테스트")
    void getNonverbalCommunicationReport_GazePointsNull() {
        // Given
        String interviewId = "test-interview-id";
        String combineId = "test-combine-id";

        Answer answer = createMockAnswer(5, 3, 2, 0.8f, 10, 15.0f);
        when(answer.getGazePoints()).thenReturn(null); // GazePoints를 null로 설정
        List<Answer> answers = Arrays.asList(answer);

        Interview interview = createMockInterview(interviewId, combineId);
        Interview updatedInterview = createMockUpdatedInterview();
        Combine combine = createMockCombine(combineId);
        ReportDto.NonverbalCommunicationReportDto expectedDto = createMockReportDto();

        // When
        when(answerRepository.findAllByInterviewId(interviewId)).thenReturn(answers);
        when(interviewRepository.findById(interviewId)).thenReturn(Optional.of(interview));
        when(interview.updateNonverbalCommunicationReport(anyFloat(), anyFloat(), anyFloat(),
                anyFloat(), anyFloat(), anyFloat(), anyList())).thenReturn(updatedInterview);
        when(combineRepository.findById(combineId)).thenReturn(Optional.of(combine));
        try (MockedStatic<ReportConverter> mocked = mockStatic(ReportConverter.class)) {
            mocked.when(() -> ReportConverter.toNonverbalCommunicationReportDto(updatedInterview, combine))
                    .thenReturn(expectedDto);

            // When
            ReportDto.NonverbalCommunicationReportDto result =
                    reportService.getNonverbalCommunicationReport(interviewId);

            // Then
            assertThat(result).isNotNull();
        }

        // gazePointsList에 빈 리스트가 포함되어야 함
        ArgumentCaptor<List<List<GazePoint>>> gazePointsCaptor =
                ArgumentCaptor.forClass(List.class);
        verify(interview).updateNonverbalCommunicationReport(anyFloat(), anyFloat(), anyFloat(),
                anyFloat(), anyFloat(), anyFloat(), gazePointsCaptor.capture());

        List<List<GazePoint>> capturedGazePoints = gazePointsCaptor.getValue();
        assertThat(capturedGazePoints).hasSize(1);
        assertThat(capturedGazePoints.get(0)).isEmpty();
    }

    // Helper methods
    private Answer createMockAnswer(int shoulderTilt, int turnLeft, int turnRight,
                                    float smileRatio, int gazeDown, float blinksPerMinute) {
        Answer answer = mock(Answer.class);
        when(answer.getShoulderTiltCount()).thenReturn(shoulderTilt);
        when(answer.getTurnLeftCount()).thenReturn(turnLeft);
        when(answer.getTurnRightCount()).thenReturn(turnRight);
        when(answer.getSmileRatio()).thenReturn(smileRatio);
        when(answer.getGazeDownCount()).thenReturn(gazeDown);
        when(answer.getBlinksPerMinute()).thenReturn(blinksPerMinute);
        when(answer.getGazePoints()).thenReturn(Arrays.asList(
                createRealGazePoint(5, 3),
                createRealGazePoint(7, 4)
        ));
        return answer;
    }

    private Answer createMockAnswerWithNullFields() {
        Answer answer = mock(Answer.class);
        when(answer.getShoulderTiltCount()).thenReturn(null); // null 필드
        return answer;
    }

    private GazePoint createRealGazePoint(int x, int y) {
        return new GazePoint(x, y, LocalDateTime.now());
    }

    private Interview createMockInterview(String interviewId, String combineId) {
        Interview interview = mock(Interview.class);
        when(interview.getCombineId()).thenReturn(combineId);
        return interview;
    }

    private Interview createMockUpdatedInterview() {
        return mock(Interview.class);
    }

    private Combine createMockCombine(String combineId) {
        Combine combine = mock(Combine.class);
        return combine;
    }

    private ReportDto.NonverbalCommunicationReportDto createMockReportDto() {
        return mock(ReportDto.NonverbalCommunicationReportDto.class);
    }
}