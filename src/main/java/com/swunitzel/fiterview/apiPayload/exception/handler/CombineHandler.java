package com.swunitzel.fiterview.apiPayload.exception.handler;

import com.swunitzel.fiterview.apiPayload.code.BaseErrorCode;
import com.swunitzel.fiterview.apiPayload.exception.GeneralException;

public class CombineHandler extends GeneralException {
    public CombineHandler(BaseErrorCode code) {
        super(code);
    }
}
