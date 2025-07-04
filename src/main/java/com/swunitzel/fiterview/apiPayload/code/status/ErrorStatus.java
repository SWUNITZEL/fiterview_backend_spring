package com.swunitzel.fiterview.apiPayload.code.status;

import com.swunitzel.fiterview.apiPayload.code.BaseErrorCode;
import com.swunitzel.fiterview.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 생활기록부 오류
    _SCHOOL_RECORD_NOT_FOUND(HttpStatus.BAD_REQUEST, "SCHOOL-RECORD403", "사용자의 생활기록부 분석 정보가 존재하지 않습니다"),

    // 인터뷰 관련 오류
    _INTERVIEW_NOT_FOUND(HttpStatus.BAD_REQUEST, "INTERVIEW404", "인터뷰를 찾을 수 없습니다"),

    // answer 관련 오류
    _ANSWER_NOT_FOUND(HttpStatus.BAD_REQUEST, "ANSWER404", "답변을 찾을 수 없습니다"),
    _ANSWER_FILED_IS_NULL(HttpStatus.BAD_REQUEST, "ANSWER405", "답변의 필드가 null입니다."),

    // combine 관련 오류
    _COMBINE_NOT_FOUND(HttpStatus.BAD_REQUEST, "COMBINE404", "면접 조합을 찾을 수 없습니다"),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }

}
