package com.yk.mqcenter.exception;

public class MqServiceException extends RuntimeException {

    private Integer code;

    public MqServiceException() {
        super();
    }

    public MqServiceException(String message) {
        super(message);
    }

    public MqServiceException(MqCode code, String message) {
        super(message);
        this.code = code.getCode();
    }

    public MqServiceException(MqCode code) {
        super(code.getMessage());
        this.code = code.getCode();
    }

    public MqServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public MqServiceException(Throwable cause) {
        super(cause);
    }

}
