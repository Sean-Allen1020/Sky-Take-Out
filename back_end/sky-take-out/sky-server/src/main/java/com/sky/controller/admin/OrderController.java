package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "订单管理接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单搜索
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("订单搜索，参数：{}", ordersPageQueryDTO);
        return Result.success(orderService.conditionSearch(ordersPageQueryDTO));
    }

    /**
     * 订单状态统计
     *
     * @return
     */
    @GetMapping("/statistics")
    @ApiOperation("订单状态统计")
    public Result<OrderStatisticsVO> statistics() {
        return Result.success(orderService.statistics());
    }

    @GetMapping("/details/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> details(@PathVariable("id") Long orderId) {
        log.info("查询订单详情，订单id：{}", orderId);
        return Result.success(orderService.details(orderId));
    }

    /**
     * 确认订单
     *
     * @param ordersConfirmDTO
     * @return
     */
    @PutMapping("/confirm")
    @ApiOperation("确认订单")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("确认订单，订单id {}, 状态 {}", ordersConfirmDTO.getId(), ordersConfirmDTO.getStatus());
        orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }

    /**
     * 拒绝订单
     *
     * @param ordersRejectionDTO
     * @return
     */
    @PutMapping("/rejection")
    @ApiOperation("拒绝订单")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        log.info("拒绝订单，订单id {}, 原因 {}", ordersRejectionDTO.getId(), ordersRejectionDTO.getRejectionReason());
        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * 取消订单
     *
     * @param ordersCancelDTO
     * @return
     */
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        log.info("取消订单，订单id {}, 原因 {}", ordersCancelDTO.getId(), ordersCancelDTO.getCancelReason());
        orderService.cancel(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 派送订单
     * @param orderId
     * @return
     */
    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public Result delivery(@PathVariable("id") Long orderId) {
        log.info("派送订单，订单id：{}", orderId);
        orderService.delivery(orderId);
        return Result.success();
    }

    /**
     * 完成订单
     * @param orderId
     * @return
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result complete(@PathVariable("id") Long orderId) {
        log.info("完成订单，订单id：{}", orderId);
        orderService.complete(orderId);
        return Result.success();
    }
}
