package com.swunitzel.fiterview.apiPayload.exception.handler;

import com.swunitzel.fiterview.apiPayload.code.BaseErrorCode;
import com.swunitzel.fiterview.apiPayload.exception.GeneralException;

public class AnswerHandler extends GeneralException {
    public AnswerHandler(BaseErrorCode code) {
        super(code);
    }
}
