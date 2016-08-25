package com.yanghongbo.model.dto;

import com.yanghongbo.model.enums.SeckillStatEnum;

/**
 * Created by yanghongbo on 16/8/6.
 */
public class SeckillExecution {
    private long seckillId;

    private int state;

    private String stateInfo;

    private SuccessKilledDto successKilled;

    public SeckillExecution(long seckillId, SeckillStatEnum statEnum, SuccessKilledDto successKilled) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    public SeckillExecution(long seckillId, SeckillStatEnum statEnum) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilledDto getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilledDto successKilled) {
        this.successKilled = successKilled;
    }
}
