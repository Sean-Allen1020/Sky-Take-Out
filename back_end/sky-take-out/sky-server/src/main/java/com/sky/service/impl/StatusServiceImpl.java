package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class StatusServiceImpl implements StatusService {

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 设置店铺营业状态
     * @param status
     * @return
     */
    public void setStatus(Integer status) {
        redisTemplate.opsForValue().set(StatusConstant.KEY, status);
    }

    /**
     * 获取店铺状态
     * @return
     */
    public Integer getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(StatusConstant.KEY);
        return status;
    }
}
