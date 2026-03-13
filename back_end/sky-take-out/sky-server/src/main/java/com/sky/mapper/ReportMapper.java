package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface ReportMapper {

    /**
     * 获取指定日期的营业额
     * @param dateTimeMap
     * @return
     */
//    @Select("select sum(amount) from orders where status = #{status} and order_time >= #{dateBegin} and order_time < #{dateEnd}")
    Double sumTurnoverByDay(Map dateTimeMap);

    /**
     * 获取指定日期的新增用户，以及日期截至的总用户数量
     * @param dateTimeMap
     * @return
     */
    Integer countUser(Map dateTimeMap);

    /**
     * 获取订单相关统计数据
     * @param dateTimeMap
     * @return
     */
    Integer countOrder(Map dateTimeMap);

    /**
     * 获取销量前十菜品
     * @param map
     * @return
     */
    List<GoodsSalesDTO> getSalesTop10(Map map);
}
