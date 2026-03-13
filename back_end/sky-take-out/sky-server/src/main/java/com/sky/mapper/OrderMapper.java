package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.*;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    /**
     * 插入订单
     *
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     *
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     *
     * @param orders
     */
    void update(Orders orders);

    /**
     * 订单分页查询
     *
     * @param ordersPageQueryDTO
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 用户端订单分页查询
     *
     * @param userOrdersPageQueryDTO
     * @return
     */
    Page<Orders> pageQueryUser(UserOrdersPageQueryDTO userOrdersPageQueryDTO);

    /**
     * 根据id查询订单
     *
     * @param orderId
     * @return
     */
    @Select("select * from orders where id = #{orderId}")
    Orders getById(Long orderId);

    /**
     * 订单状态统计
     *
     * @return
     */
    @Select("select count(*) from orders where status = #{status}")
    Integer countByStatus(Integer status);

    /**
     * 修改订单状态为确认
     *
     * @param ordersConfirmDTO
     */
    @Update("update orders set status = #{status} where id = #{id} and status = 2")
    Integer updateToBeConfirmed(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 修改订单状态为拒绝
     *
     * @param ordersRejectionDTO
     */
    @Update("update orders set status = #{status}, rejection_reason = #{rejectionReason}, cancel_time = #{cancelTime} where id = #{id} and status = 2")
    Integer updateToBeRejected(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * 修改订单状态为取消
     *
     * @param ordersCancelDTO
     * @return
     */
    @Update("update orders set status = #{status}, cancel_reason = #{cancelReason}, cancel_time = #{cancelTime} where id = #{id} and status in (1,2,3,4)")
    Integer updateToBeCancelled(OrdersCancelDTO ordersCancelDTO);

    /**
     * 修改订单状态为派送中
     *
     * @param orderDeliveryDTO
     * @return
     */
    @Update("update orders set status = #{status} where id = #{id} and status = 3")
    Integer updateToDelivery(OrderDeliveryDTO orderDeliveryDTO);

    /**
     * 修改订单状态为完成
     *
     * @param orderCompleteDTO
     * @return
     */
    @Update("update orders set status = #{status}, delivery_time = #{deliveryTime} where id = #{id} and status = 4 and pay_status = 1")
    Integer updateToComplete(OrderCompleteDTO orderCompleteDTO);

    /**
     * 修改超时订单状态为取消
     * @param order
     * @return
     */
    @Update("update orders set status = #{status}, cancel_reason = #{cancelReason}, cancel_time = #{cancelTime} where status = 1 and pay_status = 0 and order_time < now() - interval 15 minute")
    Integer cancelTimeoutOrders(Orders order);

    /**
     * 修改长期未完成的派送中订单为完成
     * @param order
     * @return
     */
    @Update("update orders set status = #{status}, delivery_time = #{deliveryTime} where status = 4 and order_time <= curdate()")
    Integer completeDeliveryOrders(Orders order);
}