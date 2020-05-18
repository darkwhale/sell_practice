package org.zxy.sell.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zxy.sell.dataobject.OrderDetail;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class OrderDetailRepositoryTest {

    @Autowired
    private OrderDetailRepository repository;

    @Test
    void save() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId("123456");
        orderDetail.setDetailId("123456788");
        orderDetail.setProductId("123459");
        orderDetail.setProductQuantity(2);
        orderDetail.setProductPrice(new BigDecimal(3.2));
        orderDetail.setProductName("皮蛋粥");
        orderDetail.setProductIcon("http://123456.jpg");

        OrderDetail result = repository.save(orderDetail);
        log.info("结果是: {}", result);

    }

    @Test
    void findByOrderId() {
        List<OrderDetail> result = repository.findByOrderId("123456");
        log.info("结果是: {}", result);

    }
}