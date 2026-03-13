package com.sky.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderCompleteDTO {

    private Long id;

    //订单状态 1待付款 2待接单 3 已接单 4 派送中 5 已完成 6 已取消 7 退款
    private Integer status;

    //送达时间
    private LocalDateTime deliveryTime;
}
