package com.sky.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class RedisCacheUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 从redis中获取菜品列表
     *
     * @param key
     * @return
     */
    public <T> List<T> getDishVOListFromRedis(String key) {

        //检查redis数据库是否有数据
        List<T> list = (List<T>) redisTemplate.opsForValue().get(key);
        //如果有数据，直接返回数据
        if (list != null) {
            return list;
        }
        return null;
    }

    /**
     * 将菜品列表存入redis
     *
     * @param key
     * @param list
     */
    public <T> void setDishVOListToRedis(String key, List<T> list) {

        //集合转换为json
        Object dishVOListJson = JSON.toJSON(list);
        redisTemplate.opsForValue().set(key, dishVOListJson, 20, java.util.concurrent.TimeUnit.MINUTES);
    }

    /**
     * 批量清理菜品缓存
     */
    public void clearCacheFromRedis() {

        Set keys = redisTemplate.keys("category_*");
        redisTemplate.delete(keys);
    }

    /**
     * 清理某个分类下的菜品缓存
     *
     * @param key
     */
    public void clearCacheFromRedis(String key) {

        redisTemplate.delete(key);
    }
}
