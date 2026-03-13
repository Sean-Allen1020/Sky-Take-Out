package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 用户端订单分页查询
     * @param userOrdersPageQueryDTO
     * @return
     */
    PageResult pageQuery4User(UserOrdersPageQueryDTO userOrdersPageQueryDTO);

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    OrderVO details(Long orderId);

    /**
     * 用户取消订单
     * @param orderId
     */
    void userCancelById(Long orderId);

    /**
     * 订单重复发送
     * @param orderId
     */
    void repetition(Long orderId);

/***************************************后台管理****************************************/

    /**
     * 订单搜索
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 订单状态统计
     * @return
     */
    OrderStatisticsVO statistics();

    /**
     * 确认订单
     * @param ordersConfirmDTO
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 拒绝订单
     * @param ordersRejectionDTO
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * 取消订单
     * @param ordersCancelDTO
     */
    void cancel(OrdersCancelDTO ordersCancelDTO);

    /**
     * 订单派送
     * @param orderId
     */
    void delivery(Long orderId);

    /**
     * 订单完成
     * @param orderId
     */
    void complete(Long orderId);

    /**
     * 用户催单
     * @param orderId
     */
    void remainder(Long orderId);
}
