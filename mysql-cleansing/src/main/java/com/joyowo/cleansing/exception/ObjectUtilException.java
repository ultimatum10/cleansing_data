package com.joyowo.cleansing.exception;

/**
 * 工具类ObjectUtil遇到预想以外的情况或是内部异常时，
 * 会抛出这个异常，以交给调用方决定如何处理
 *
 * @author Deolin 2021-08-05
 */
public class ObjectUtilException extends RuntimeException {

    private static final long serialVersionUID = 2506389302288058433L;

    public ObjectUtilException() {
        super();
    }

    public ObjectUtilException(String message) {
        super(message);
    }

    public ObjectUtilException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectUtilException(Throwable cause) {
        super(cause);
    }

    protected ObjectUtilException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

