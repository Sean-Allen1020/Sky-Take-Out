package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.properties.MapProperties;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.MapUtil;
import com.sky.utils.OrderNoGenerator;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private OrderNoGenerator orderNoGenerator;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
    @Autowired
    private MapUtil mapUtil;
    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 用户下单
     *
     * @param ordersSubmitDTO
     * @return
     */
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        //0. 获取当前用户id
        Long userId = BaseContext.getCurrentId();

        //1. 处理各种业务异常(如：地址不存在，购物车为空)
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(ShoppingCart.builder().userId(userId).build());
        if (shoppingCartList == null || shoppingCartList.isEmpty()) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //1.1. 获取用户坐标，判断是否超出配送范围
        if(mapUtil.isOutOfRange(addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail())){
            throw new OrderBusinessException(MessageConstant.ORDOR_OUT_OF_RANGE);
        }

        //2. 向订单表插入1条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setUserId(userId);                               //设置用户id
        orders.setOrderTime(LocalDateTime.now());               //设置下单时间
        orders.setPayStatus(Orders.UN_PAID);                    //设置支付状态，默认为未支付
        orders.setStatus(Orders.PENDING_PAYMENT);               //设置订单状态，默认为待付款
        orders.setNumber(orderNoGenerator.generateOrderNo());   //设置订单号，格式为：时间戳 + 4位Redis自增序列
        orders.setPhone(addressBook.getPhone());                //设置手机号
        orders.setConsignee(addressBook.getConsignee());        //设置收货人
        orders.setAddress(addressBook.getDistrictName() + addressBook.getDetail()); //设置地址

        orderMapper.insert(orders);

        //3. 向订单明细表插入若干条数据
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart shopingCart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shopingCart, orderDetail);
            orderDetail.setOrderId(orders.getId());

            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.batchInsert(orderDetailList);

        //4. 清空当前购物车数据
        shoppingCartMapper.deleteByUserId(userId);

        //5. 封装VO类
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();

        return orderSubmitVO;
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

//        //调用微信支付接口，生成预支付交易单
//        JSONObject jsonObject = weChatPayUtil.pay(
//                ordersPaymentDTO.getOrderNumber(), //商户订单号
//                new BigDecimal(0.01), //支付金额，单位 元
//                "苍穹外卖订单", //商品描述
//                user.getOpenid() //微信用户的openid
//        );

        //测试: 模拟生成预支付交易单
        JSONObject jsonObject = new JSONObject();

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);

        // 通过 web socket 向客户端推送消息：type orderId content
        Map map = new HashMap();
        map.put("type", 1);     //1 来单提醒，2 催单
        map.put("orderId", ordersDB.getId());
        map.put("content", "订单号: " + outTradeNo);
        // map 转换为 json字符串
        String jsonString = JSONObject.toJSONString(map);

        // 将json字符串发向客户端
        webSocketServer.sendToAllClient(jsonString);
    }

    /**
     * 用户端订单分页查询
     *
     * @param userOrdersPageQueryDTO
     * @return
     */
    public PageResult pageQuery4User(UserOrdersPageQueryDTO userOrdersPageQueryDTO) {
        // 1. 设置分页参数
        PageHelper.startPage(userOrdersPageQueryDTO.getPage(), userOrdersPageQueryDTO.getPageSize());

        // 2. 设置查询条件
        userOrdersPageQueryDTO.setUserId(BaseContext.getCurrentId());

        // 3. 分页查询
        Page<Orders> page = orderMapper.pageQueryUser(userOrdersPageQueryDTO);

        List<OrderVO> orderVOList = new ArrayList<>();
        // 4. 设置订单菜品信息，插入订单详情，封装VO
        if (page != null && !page.isEmpty()) {
            //复制分页查询结果到VO
            for (Orders order : page) {
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(order, orderVO);

                //获取订单明细列表，封装到VO
                Long orderId = order.getId();
                List<OrderDetail> orderDetail = orderDetailMapper.getByOrderId(orderId);
                orderVO.setOrderDetailList(orderDetail);

                orderVOList.add(orderVO);
            }
        }
        // 4. 返回结果
        return new PageResult(page.getTotal(), orderVOList);
    }

    /**
     * 订单详情
     *
     * @param orderId
     * @return
     */
    public OrderVO details(Long orderId) {
        OrderVO orderVO = new OrderVO();
        Orders orders = orderMapper.getById(orderId);
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailMapper.getByOrderId(orderId));
        return orderVO;
    }

    /**
     * 用户取消订单
     *
     * @param orderId
     */
    public void userCancelById(Long orderId) {

        // 1. 查询订单状态
        Orders order = orderMapper.getById(orderId);
        if (order == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        Integer status = order.getStatus();

        // 2. 创建要更新的订单对象
        Orders orderToCancel = new Orders();
        orderToCancel.setId(orderId);

        // 3. 根据订单状态，处理不同的取消逻辑
        // 订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
        if (status >= 3) {
            // 商家已接单和派送中状态下，用户取消订单需电话沟通商家
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR_CONTACT_MERCHANT);
        }
        if (status.equals(Orders.TO_BE_CONFIRMED)) {
//            //调用微信支付退款接口
//                weChatPayUtil.refund(
//                        order.getNumber(), //商户订单号
//                        order.getNumber(), //商户退款单号
//                        new BigDecimal(0.01),//退款金额，单位 元
//                        new BigDecimal(0.01));//原订单金额
            // 待接单状态下需要退款
            orderToCancel.setPayStatus(Orders.REFUND);
        }
        // 取消订单
        orderToCancel.setStatus(Orders.CANCELLED);
        orderToCancel.setCancelReason("用户取消");
        orderToCancel.setCancelTime(LocalDateTime.now());
        orderMapper.update(orderToCancel);
    }

    /**
     * 订单重复发送
     *
     * @param orderId
     */
    public void repetition(Long orderId) {
        // 获取订单明细
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orderId);
        // 将订单明细添加到购物车
        List<ShoppingCart> shoppingCartList = orderDetailList.stream().map(orderDetail -> {
            ShoppingCart shoppingCart = new ShoppingCart();

            BeanUtils.copyProperties(orderDetail, shoppingCart);
            shoppingCart.setUserId(BaseContext.getCurrentId());
            shoppingCart.setCreateTime(LocalDateTime.now());

            return shoppingCart;
        }).collect(Collectors.toList());

        shoppingCartMapper.batchInsert(shoppingCartList);
    }

    /**
     * 用户催单
     * @param orderId
     */
    @Override
    public void remainder(Long orderId) {
        Orders ordersDB = orderMapper.getById(orderId);
        if(ordersDB == null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        Map map = new HashMap();
        map.put("type", 2);     //1 来单提醒，2 催单
        map.put("orderId", orderId);
        map.put("content", "订单号: " + ordersDB.getNumber());
        // map 转换为 json字符串
        String jsonString = JSONObject.toJSONString(map);

        // 将json字符串发向客户端
        webSocketServer.sendToAllClient(jsonString);
    }

/***************************************后台管理****************************************/

    /**
     * 订单搜索
     *
     * @param ordersPageQueryDTO
     * @return
     */
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);

        List<OrderVO> orderVOList = new ArrayList<>();
        for (Orders order : page) {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            orderVO.setOrderDishes(getOrderDishesStr(order));
            orderVOList.add(orderVO);
        }
        return new PageResult(page.getTotal(), orderVOList);
    }

    /**
     * 订单状态统计
     *
     * @return
     */
    @Override
    public OrderStatisticsVO statistics() {

        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        // 待接单数量
        orderStatisticsVO.setToBeConfirmed(orderMapper.countByStatus(Orders.TO_BE_CONFIRMED));
        // 待派送数量
        orderStatisticsVO.setConfirmed(orderMapper.countByStatus(Orders.CONFIRMED));
        // 派送中数量
        orderStatisticsVO.setDeliveryInProgress(orderMapper.countByStatus(Orders.DELIVERY_IN_PROGRESS));

        return orderStatisticsVO;
    }

    /**
     * 确认订单
     *
     * @param ordersConfirmDTO
     */
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        Orders order = orderMapper.getById(ordersConfirmDTO.getId());
        if (order == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        // 只允许待接单状态下确认订单
        if (!order.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        // 修改订单状态为已接单
        ordersConfirmDTO.setStatus(Orders.CONFIRMED);
        Integer row = orderMapper.updateToBeConfirmed(ordersConfirmDTO);
        // 判断是否修改成功
        if (row == null || row <= 0) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
    }

    /**
     * 拒绝订单
     *
     * @param ordersRejectionDTO
     */
    @Transactional
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) {
        Orders order = orderMapper.getById(ordersRejectionDTO.getId());
        if (order == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        // 只允许待接单状态下拒绝订单
        if (!order.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        // 修改订单状态为已取消
        ordersRejectionDTO.setStatus(Orders.CANCELLED);
        ordersRejectionDTO.setCancelTime(LocalDateTime.now());
        Integer row = orderMapper.updateToBeRejected(ordersRejectionDTO);
        // 判断是否修改成功
        if (row == null || row <= 0) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        //如果已支付，则需要退款
        if (order.getPayStatus().equals(Orders.PAID)) {
            Orders orderToRefund = new Orders();
            orderToRefund.setId(order.getId());
            orderToRefund.setPayStatus(Orders.REFUND);
            orderMapper.update(orderToRefund);
        }
        //支付状态（测试期间，不需使用）
//        Integer payStatus = order.getPayStatus();
//        if (payStatus == Orders.PAID) {
//            //用户已支付，需要退款
//            String refund = weChatPayUtil.refund(
//                    order.getNumber(),
//                    order.getNumber(),
//                    new BigDecimal(0.01),
//                    new BigDecimal(0.01));
//            log.info("申请退款：{}", refund);
//        }
    }

    /**
     * 取消订单
     *
     * @param ordersCancelDTO
     */
    @Transactional
    public void cancel(OrdersCancelDTO ordersCancelDTO) {
        Orders order = orderMapper.getById(ordersCancelDTO.getId());
        if (order == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        // 只要状态不是已取消，都可以取消订单
        if (order.getStatus() < 5) {
            // 修改订单状态为已取消
            ordersCancelDTO.setStatus(Orders.CANCELLED);
            ordersCancelDTO.setCancelTime(LocalDateTime.now());
            Integer row = orderMapper.updateToBeCancelled(ordersCancelDTO);
            // 判断是否修改成功
            if (row == null || row <= 0) {
                throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
            }
            //如果已支付，则需要退款
            if (order.getPayStatus().equals(Orders.PAID)) {
                Orders orderToRefund = new Orders();
                orderToRefund.setId(order.getId());
                orderToRefund.setPayStatus(Orders.REFUND);
                orderMapper.update(orderToRefund);
            }
            //支付状态（测试期间，不需使用）
//        Integer payStatus = order.getPayStatus();
//        if (payStatus == Orders.PAID) {
//            //用户已支付，需要退款
//            String refund = weChatPayUtil.refund(
//                    order.getNumber(),
//                    order.getNumber(),
//                    new BigDecimal(0.01),
//                    new BigDecimal(0.01));
//            log.info("申请退款：{}", refund);
//        }
        } else {
            // 已取消状态下，不能重复取消订单
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
    }

    /**
     * 派送订单
     *
     * @param orderId
     */
    public void delivery(Long orderId) {
        Orders order = orderMapper.getById(orderId);
        if (order == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        // 只允许已接单状态下派送订单
        if (!order.getStatus().equals(Orders.CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        // 修改订单状态为派送中
        OrderDeliveryDTO orderDeliveryDTO = new OrderDeliveryDTO();
        orderDeliveryDTO.setId(orderId);
        orderDeliveryDTO.setStatus(Orders.DELIVERY_IN_PROGRESS);
        Integer row = orderMapper.updateToDelivery(orderDeliveryDTO);
        // 判断是否修改成功
        if (row == null || row <= 0) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
    }

    /**
     * 完成订单
     *
     * @param orderId
     */
    public void complete(Long orderId) {
        Orders order = orderMapper.getById(orderId);
        if (order == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        // 只允许派送中，且已支付状态下完成订单
        if (!order.getStatus().equals(Orders.DELIVERY_IN_PROGRESS) || !order.getPayStatus().equals(Orders.PAID)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        // 修改订单状态为已完成
        OrderCompleteDTO orderCompleteDTO = new OrderCompleteDTO();
        orderCompleteDTO.setId(orderId);
        orderCompleteDTO.setStatus(Orders.COMPLETED);
        orderCompleteDTO.setDeliveryTime(LocalDateTime.now());
        Integer row = orderMapper.updateToComplete(orderCompleteDTO);
        // 判断是否修改成功
        if (row == null || row <= 0) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
    }

    /**
     * 获取订单菜品名称拼接字符串
     *
     * @param order
     * @return
     */
    private String getOrderDishesStr(Orders order) {
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(order.getId());
        String orderDishes = null;
        if (orderDetailList != null && !orderDetailList.isEmpty()) {
            orderDishes = orderDetailList.stream().map(orderDetail -> orderDetail.getName() + "*" + orderDetail.getNumber()).collect(Collectors.joining(", "));
        }
        return orderDishes;
    }
}


/// **
// * 用户端订单分页查询
// *
// * @param ordersPageQueryDTO
// * @return
// */
//    public PageResult pageQuery4User(OrdersPageQueryDTO ordersPageQueryDTO) {
//
//        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
//        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
//        Page<OrderVO> p = orderMapper.pageQuery(ordersPageQueryDTO);
//
//        if (p != null && !p.isEmpty()) {
//            for (OrderVO orderVO : p) {
//                List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orderVO.getId());
//                orderVO.setOrderDetailList(orderDetailList);
//            }
//        }
//        return new PageResult(p.getTotal(), p.getResult());
//    }
//}
