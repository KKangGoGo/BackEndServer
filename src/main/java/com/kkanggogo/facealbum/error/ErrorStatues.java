package com.kkanggogo.facealbum.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum ErrorStatues {
    BAD_REQUEST(HttpStatus.BAD_REQUEST,Arrays.asList(ErrorCode.E4001)),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, Arrays.asList(ErrorCode.E4011,ErrorCode.E4012,ErrorCode.E4013)),
    FORBIDDEN(HttpStatus.FORBIDDEN,Arrays.asList(ErrorCode.E4031)),
    CONFLICT(HttpStatus.CONFLICT,Arrays.asList(ErrorCode.E4091)),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,Arrays.asList(ErrorCode.E5001));
    private HttpStatus httpStatus;

    private List<ErrorCode> errorCodeList;


    ErrorStatues(HttpStatus httpStatus, List<ErrorCode> errorCodeList) {
        this.httpStatus = httpStatus;
        this.errorCodeList = errorCodeList;
    }

    public static ErrorStatues findByErrorCode(ErrorCode errorCode){
        ErrorStatues statues = Arrays.stream(ErrorStatues.values()).filter(errorStatues -> errorStatues.hasErrorCode(errorCode))
                .findAny().orElse(INTERNAL_SERVER_ERROR);
        return statues;
    }

    private boolean hasErrorCode(ErrorCode errorCode) {
        return errorCodeList.stream().anyMatch(code->code==errorCode);
    }

    public int getHttpStateCode(){
        return this.httpStatus.value();
    }
}
