package org.zxy.sell.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zxy.sell.dto.OrderDTO;

@SpringBootTest
@Slf4j
class PayServiceImplTest {

    @Autowired
    private PayServiceImpl payService;

    @Autowired
    private OrderServiceImpl orderService;

    @Test
    void create() {
        OrderDTO orderDTO = orderService.findOne("1589865086994345757");
        payService.create(orderDTO);
    }
}