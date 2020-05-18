package org.zxy.sell.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.zxy.sell.dataobject.ProductInfo;
import org.zxy.sell.dto.CartDTO;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
class ProductServiceImplTest {

    @Autowired
    private ProductServiceImpl productService;

    @Test
    void findAll() {
        Page<ProductInfo> productInfoPage = productService.findAll(PageRequest.of(0, 2));
        log.info("查询结果是 {}", productInfoPage.getTotalElements());
    }

    @Test
    void decreaseStock() {
        List<CartDTO> cartDTOList = new ArrayList<>();
        CartDTO cartDTO = new CartDTO();
        cartDTO.setProductId("123456");
        cartDTO.setProductQuantity(100);
        cartDTOList.add(cartDTO);
        productService.decreaseStock(cartDTOList);
    }
}