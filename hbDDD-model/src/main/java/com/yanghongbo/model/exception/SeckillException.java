package com.yanghongbo.model.exception;

/**
 * Created by yanghongbo on 16/8/25.
 */
public class SeckillException extends RuntimeException{
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}