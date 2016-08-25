package com.yanghongbo.application.impl;

import com.yanghongbo.application.SeckillApplication;
import com.yanghongbo.domain.Seckill;
import com.yanghongbo.domain.SuccessKilled;
import com.yanghongbo.model.dto.Exposer;
import com.yanghongbo.model.dto.SeckillExecution;
import com.yanghongbo.model.dto.SuccessKilledDto;
import com.yanghongbo.model.enums.SeckillStatEnum;
import com.yanghongbo.model.exception.RepeatKillException;
import com.yanghongbo.model.exception.SeckillCloseException;
import com.yanghongbo.model.exception.SeckillException;
import com.yanghongbo.repository.SeckillDao;
import com.yanghongbo.repository.SuccessKilledDao;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by yanghongbo on 16/8/25.
 */
@Service
public class SeckillApplicationImpl  implements SeckillApplication {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    private final String slat = "asdfasfd2342343q23";

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 5);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        if (seckill == null) {
            return new Exposer(false, seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        String md5 = getMD5(seckillId);
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, md5,seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }

        try {
            int updateCount = seckillDao.reduceNumber(seckillId, new Date());
            if (updateCount <= 0) {
                throw new SeckillCloseException("seckill is  closed");
            } else {
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                if (insertCount <= 0) {
                    throw new RepeatKillException("seckill repeated");
                } else {
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    SuccessKilledDto successKilledDto=new SuccessKilledDto();
                    successKilledDto.setCreateTime(successKilled.getCreateTime());
                    successKilledDto.setSeckillId(successKilled.getSeckillId());
                    successKilledDto.setState(successKilled.getState());
                    successKilledDto.setUserPhone(successKilled.getUserPhone());
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilledDto);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
    }
}

