package com.yanghongbo.application;

import com.yanghongbo.domain.Seckill;
import com.yanghongbo.model.dto.Exposer;
import com.yanghongbo.model.dto.SeckillExecution;
import com.yanghongbo.model.exception.RepeatKillException;
import com.yanghongbo.model.exception.SeckillCloseException;
import com.yanghongbo.model.exception.SeckillException;

import java.util.List;

/**
 * Created by yanghongbo on 16/8/25.
 */
public interface SeckillApplication {
    List<Seckill> getSeckillList();

    Seckill getById(long seckillId);

    Exposer exportSeckillUrl(long seckillId);
    //hehe
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException,RepeatKillException,SeckillCloseException;

}
