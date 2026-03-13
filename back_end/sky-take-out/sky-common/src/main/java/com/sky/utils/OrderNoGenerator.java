package com.sky.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * 通过Redis自增序列，实现订单号生成
 *
 * 选择Redis的原因：
 *      - Redis是内存数据，简单的数据处理交给内存会带来极大的性能提生
 *      - Redis是单线程处理，并发环境下数据排队处理，避免重复订单号
 *      - 可以设置过期时间，减少数据压力
 *      - Java代码实现的自增会出现并发重复，即便加了synchronized锁，高并发下也会出现锁竞争
 *      - 数据库自增序列会在订单大量出现时会带来压力，并且同样会出现锁竞争等性能问题
 */
@Component
public class OrderNoGenerator {

    @Autowired
    private RedisTemplate redisTemplate;

    public String generateOrderNo() {

        //获取时间戳
        long timestamp = System.currentTimeMillis();

        //获取序列号的key，格式为：order_no:day
        String day = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now());
        String key = "order_no:" + day;

        //获取自增序列号
        Long increment = redisTemplate.opsForValue().increment(key);

        //设置自增序列号过期时间为1天 (if用于防止重复设置过期时间覆导致的永久不过期)
        if(increment != null && increment == 1L){
            redisTemplate.expire(key, 1, TimeUnit.DAYS);
        }
        //获取订单号，格式： 时间戳 + 序列号(补齐4位)
        String orderNo = String.format("%d%04d", timestamp, increment);
        return orderNo;
    }
}
