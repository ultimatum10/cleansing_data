package com.lyn.cleansing.exception;

/**
 * 清洗数据异常类
 *
 * @author lyn
 * @date 2023/8/8
 */
public class CleansingException extends RuntimeException {

    private static final long serialVersionUID = 2506389302288051229L;

    public CleansingException() {
        super();
    }

    public CleansingException(String message) {
        super(message);
    }

    public CleansingException(String message, Throwable cause) {
        super(message, cause);
    }

    public CleansingException(Throwable cause) {
        super(cause);
    }

    protected CleansingException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
