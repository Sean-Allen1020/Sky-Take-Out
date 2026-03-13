package com.sky.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class OrdersCancelDTO implements Serializable {

    private Long id;

    //订单状态 1待付款 2待接单 3 已接单 4 派送中 5 已完成 6 已取消 7 退款
    private Integer status;

    //订单取消原因
    private String cancelReason;

    //取消时间
    private LocalDateTime cancelTime;

}
