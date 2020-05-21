package org.zxy.sell.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zxy.sell.dto.OrderDTO;
import org.zxy.sell.enums.ExceptionEnum;
import org.zxy.sell.exception.SellException;
import org.zxy.sell.service.impl.OrderServiceImpl;

@Controller
@Slf4j
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private OrderServiceImpl orderService;

    @GetMapping("/create")
    public void create(@RequestParam("orderId") String orderId,
                       @RequestParam("returnUrl") String returnUrl) {
        OrderDTO orderDTO = orderService.findOne(orderId);
        if (orderDTO == null) {
            throw new SellException(ExceptionEnum.ORDER_NOT_EXIST);
        }

        // 发起支付;

    }
}
