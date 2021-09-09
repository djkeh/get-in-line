package com.uno.getinline.exception;

import com.uno.getinline.constant.ErrorCode;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private final ErrorCode errorCode;

    public GeneralException() {
        super(ErrorCode.INTERNAL_ERROR.getMessage());
        this.errorCode = ErrorCode.INTERNAL_ERROR;
    }

    public GeneralException(String message) {
        super(ErrorCode.INTERNAL_ERROR.getMessage(message));
        this.errorCode = ErrorCode.INTERNAL_ERROR;
    }

    public GeneralException(String message, Throwable cause) {
        super(ErrorCode.INTERNAL_ERROR.getMessage(message), cause);
        this.errorCode = ErrorCode.INTERNAL_ERROR;
    }

    public GeneralException(Throwable cause) {
        super(ErrorCode.INTERNAL_ERROR.getMessage(cause));
        this.errorCode = ErrorCode.INTERNAL_ERROR;
    }

    public GeneralException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public GeneralException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(cause), cause);
        this.errorCode = errorCode;
    }

}
