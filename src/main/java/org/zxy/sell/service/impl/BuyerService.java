package org.zxy.sell.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zxy.sell.dto.OrderDTO;
import org.zxy.sell.enums.ExceptionEnum;
import org.zxy.sell.exception.SellException;
import org.zxy.sell.service.IBuyerService;

@Service
@Slf4j
public class BuyerService implements IBuyerService {

    @Autowired
    private OrderServiceImpl orderService;

    @Override
    public OrderDTO findOrderOne(String openid, String orderId) {
        return checkOwner(orderId, openid);
    }

    @Override
    public OrderDTO cancelOrder(String openid, String orderId) {
        return orderService.cancel(checkOwner(orderId, openid));
    }

    private OrderDTO checkOwner(String orderId, String openid) {
        OrderDTO orderDTO = orderService.findOne(orderId);
        if (orderDTO == null) {
            log.error("[查询订单], 查询不到该订单, orderId={}", orderId);
            throw new SellException(ExceptionEnum.ORDER_NOT_EXIST);
        }
        if (!orderDTO.getBuyerOpenid().equalsIgnoreCase(openid)) {
            log.error("[订单], 订单openid不一致，orderId={}", orderId);
            throw new SellException(ExceptionEnum.OPENID_ERROR);
        }
        return  orderDTO;
    }
}
