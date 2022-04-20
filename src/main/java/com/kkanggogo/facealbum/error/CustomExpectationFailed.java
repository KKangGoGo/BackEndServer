package com.kkanggogo.facealbum.error;

import org.springframework.validation.Errors;

public class CustomExpectationFailed extends CustomException {

    private static final long serialVersionUID = -2116671122895194105L;

    public CustomExpectationFailed() {
        super(ErrorCode.EXPECTATION_FAILED);
    }
}
