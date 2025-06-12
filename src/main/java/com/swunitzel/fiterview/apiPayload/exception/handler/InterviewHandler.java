package com.swunitzel.fiterview.apiPayload.exception.handler;

import com.swunitzel.fiterview.apiPayload.code.BaseErrorCode;
import com.swunitzel.fiterview.apiPayload.exception.GeneralException;

public class InterviewHandler extends GeneralException {
    public InterviewHandler(BaseErrorCode code) {
        super(code);
    }
}
