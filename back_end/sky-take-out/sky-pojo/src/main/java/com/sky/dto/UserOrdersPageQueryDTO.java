package com.sky.dto;

import lombok.Data;

@Data
public class UserOrdersPageQueryDTO {

    private int page;

    private int pageSize;

    private Long userId;

    private Integer status;
}
