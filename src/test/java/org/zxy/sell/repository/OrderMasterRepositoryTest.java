package org.zxy.sell.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.zxy.sell.dataobject.OrderMaster;

import java.math.BigDecimal;

@SpringBootTest
@Slf4j
class OrderMasterRepositoryTest {

    @Autowired
    private OrderMasterRepository repository;

    private final String OPENID = "110110";

    @Test
    void save() {
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("123457");
        orderMaster.setBuyerName("周心扬");
        orderMaster.setBuyerPhone("15527957708");
        orderMaster.setBuyerAddress("武汉");
        orderMaster.setBuyerOpenid(OPENID);
        orderMaster.setOrderAmount(new BigDecimal(2.5));

        OrderMaster result = repository.save(orderMaster);
        log.info("结果： {}", result);
    }

    @Test
    void findByBuyerOpenid() {

        Page<OrderMaster> result = repository.findByBuyerOpenid(OPENID, PageRequest.of(0, 1));

        log.info("结果： {}", result.getTotalElements());
    }
}