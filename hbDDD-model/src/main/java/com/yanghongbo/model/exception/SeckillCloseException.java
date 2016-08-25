package com.yanghongbo.model.exception;

/**
 * Created by yanghongbo on 16/8/25.
 */
public class SeckillCloseException  extends SeckillException{
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}