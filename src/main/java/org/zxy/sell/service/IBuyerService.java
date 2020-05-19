package org.zxy.sell.service;

import org.zxy.sell.dto.OrderDTO;

public interface IBuyerService {
    OrderDTO findOrderOne(String openid, String orderId);

    OrderDTO cancelOrder(String openid, String orderId);
}
