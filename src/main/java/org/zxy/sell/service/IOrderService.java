package org.zxy.sell.service;

import org.springframework.data.domain.Page;
import org.zxy.sell.dataobject.OrderMaster;
import org.zxy.sell.dto.OrderDTO;

import java.awt.print.Pageable;

public interface IOrderService {

    /**
     * 创建订单；
     */
    OrderDTO create(OrderDTO orderDTO);

    /**
     * 查询单个订单
     */
    OrderDTO findOne(String orderId);

    /**
     * 查询订单列表
     */
    Page<OrderMaster> findList(String buyerOpenid, Pageable pageable);

    /**
     * 取消订单；
     */
    OrderDTO cancel(OrderDTO orderDTO);


    /**
     * 完结订单；
     */
    OrderDTO finished(OrderDTO orderDTO);

    /**
     * 支付订单;
     */
    OrderDTO paid(OrderDTO orderDTO);
}
