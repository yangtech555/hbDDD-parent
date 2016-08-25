package com.yanghongbo.model.exception;

/**
 * Created by yanghongbo on 16/8/25.
 */
public class RepeatKillException  extends SeckillException{
    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
