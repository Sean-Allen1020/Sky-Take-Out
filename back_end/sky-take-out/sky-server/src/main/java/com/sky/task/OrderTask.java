package com.sky.task;

import com.sky.constant.MessageConstant;
import com.sky.entity.Orders;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * =========================
 * 需求分析
 * =========================
 * 用户下单后可能存在的情况：
 * 1) 下单后未支付，订单一直处于“待支付”状态
 * 2) 用户收货后管理端未点击完成按钮，订单一直处于“派送中”状态
 * <p>
 * 对于上述两种情况，需要通过定时任务来修正订单状态，具体逻辑为：
 * 1) 每分钟检查一次是否存在支付超时订单：
 * - 下单后超过15分钟仍未支付，则判定为支付超时订单
 * - 若存在则将订单状态修改为“已取消”
 * 2) 每天凌晨1点检查一次是否存在“派送中”的订单：
 * - 若存在则将订单状态修改为“已完成”
 * <p>
 * <p>
 * =========================
 * 使用 Spring Task（定时扫表）处理该业务的缺陷
 * =========================
 * 1) 数据库压力：按固定频率扫描数据库，业务量上来后会增加无效查询与更新压力
 * 2) 实时性一般：依赖扫描周期（如每分钟一次），超时处理存在“延迟到下一次扫描”的情况
 * 3) 分布式问题：多实例部署时可能出现重复执行，需要额外的分布式锁/任务协调机制
 * 4) 可扩展性较弱：订单量增大后，扫描范围与执行耗时增加，容易影响核心业务库性能
 * <p>
 * <p>
 * =========================
 * 更推荐使用 Redis 的理由
 * =========================
 * 1) 事件驱动/到期触发：把“到点处理”从扫表改为“到期触发”，减少大量无效扫描
 * 2) 降低数据库压力：到期后只处理具体订单，避免高频全表/大范围查询
 * 3) 时效更好：可以做到接近到期时刻触发（相比分钟级扫描更及时）
 * <p>
 * <p>
 * =========================
 * Redis 处理方式（简要步骤）
 * =========================
 * 场景：待支付超时自动取消（15分钟）
 * 1) 订单创建成功后，在 Redis 写入一个与订单ID绑定的 key，并设置 TTL=15分钟
 * 2) 开启 Redis 的 key 过期事件通知（Keyspace Notifications），监听 key 过期消息
 * 3) 监听到对应订单 key 过期后，执行业务处理：尝试将订单从“待支付”更新为“已取消”
 * - 更新时务必带条件（仅当订单仍为“待支付”才取消），防止误取消已支付订单（幂等保障）
 * 4) 建议保留兜底方案：定时任务低频补偿扫描，防止服务重启/监听中断导致漏处理
 * <p>
 * 场景：“派送中”订单自动完成（每天凌晨1点）
 * - 这种更偏“批处理/规则修正”，通常仍可用定时任务（低频）处理；
 * - 若要更严格/更实时，需要引入配送签收事件（如签收回调/消息事件）驱动状态流转。
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    OrderMapper orderMapper;

    /**
     * 处理超时订单
     */
    @Scheduled(cron = "0 * * * * *")
    public void processTimeoutOrder() {
        log.info("定时处理超时未付款订单：{}", LocalDateTime.now());

        Orders order = Orders.builder()
                .status(Orders.CANCELLED)
                .cancelReason("订单超时，自动取消")
                .cancelTime(LocalDateTime.now())
                .build();

        Integer row = orderMapper.cancelTimeoutOrders(order);
        // 判断是否修改成功
        if (row != null && row > 0) {
            log.info("超时订单取消数量：{}", row);
        }
    }

    /**
     * 处理超长时间派送中订单
     */
    @Scheduled(cron = "0 0 1 * * *")
    public void processDeliveryOrder(){
        log.info("定时处理派送中订单：{}", LocalDateTime.now());

        Orders order = Orders.builder()
                .status(Orders.COMPLETED)
                .deliveryTime(LocalDateTime.now())
                .build();

        Integer row = orderMapper.completeDeliveryOrders(order);
        // 判断是否修改成功
        if (row != null && row > 0) {
            log.info("处理超长派送订单数量：{}", row);
        }
    }
}

