package org.zxy.sell.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zxy.sell.dataobject.ProductInfo;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@Slf4j
class ProductInfoRepositoryTest {

    @Autowired
    private ProductInfoRepository repository;

    @Test
    void saveTest() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("123456");
        productInfo.setProductName("皮蛋粥");
        productInfo.setProductPrice(new BigDecimal(3.2));
        productInfo.setProductDescription("很好喝的粥");
        productInfo.setProductIcon("http://123456.jpg");
        productInfo.setProductStatus(0);
        productInfo.setCategoryType(4);
        productInfo.setProductStock(100);
        repository.save(productInfo);
    }

    @Test
    void findByProductStatus() {
        List<ProductInfo> productInfoList = repository.findByProductStatus(0);
        log.info("上架商品为 {}", productInfoList);
    }
}