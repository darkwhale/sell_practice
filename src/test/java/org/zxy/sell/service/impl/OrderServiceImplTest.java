package org.zxy.sell.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.zxy.sell.dataobject.OrderDetail;
import org.zxy.sell.dto.OrderDTO;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    private final String OPENID = "123456";

    private final String ORDERID = "123456";

    @Test
    void create() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName("周心扬");
        orderDTO.setBuyerOpenid(OPENID);
        orderDTO.setBuyerAddress("武汉");
        orderDTO.setBuyerPhone("15527957708");

        List<OrderDetail> orderDetailList = new ArrayList<>();
        OrderDetail o1 = new OrderDetail();
        o1.setProductId("123458");
        o1.setProductQuantity(3);
        orderDetailList.add(o1);

        orderDTO.setOrderDetailList(orderDetailList);

        OrderDTO result = orderService.create(orderDTO);
        log.info("结果是：{}", result);
    }

    @Test
    void findOne() {
        OrderDTO result = orderService.findOne(ORDERID);
        log.info("结果是：{}", result);
    }

    @Test
    void findList() {
        Page<OrderDTO> result = orderService.findList(OPENID, PageRequest.of(0, 2));
        log.info("结果是: {}", result.getContent());

    }

    @Test
    void cancel() {
        OrderDTO orderDTO = orderService.findOne(ORDERID);
        OrderDTO result = orderService.cancel(orderDTO);
        log.info("结果是: {}", result);
    }

    @Test
    void finished() {
        OrderDTO orderDTO = orderService.findOne(ORDERID);
        OrderDTO result = orderService.finished(orderDTO);
        log.info("结果是: {}", result);
    }

    @Test
    void paid() {
        OrderDTO orderDTO = orderService.findOne(ORDERID);
        OrderDTO result = orderService.paid(orderDTO);
        log.info("结果是: {}", result);
    }
}