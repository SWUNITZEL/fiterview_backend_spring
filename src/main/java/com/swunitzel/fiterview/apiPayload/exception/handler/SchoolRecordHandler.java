package com.swunitzel.fiterview.apiPayload.exception.handler;

import com.swunitzel.fiterview.apiPayload.code.BaseErrorCode;
import com.swunitzel.fiterview.apiPayload.exception.GeneralException;

public class SchoolRecordHandler extends GeneralException {
    public SchoolRecordHandler(BaseErrorCode code) {
        super(code);
    }
}
