package com.swunitzel.fiterview.apiPayload.exception.handler;

import com.swunitzel.fiterview.apiPayload.code.BaseErrorCode;
import com.swunitzel.fiterview.apiPayload.exception.GeneralException;

public class UserHandler extends GeneralException {
    public UserHandler(BaseErrorCode code) {
        super(code);
    }
}