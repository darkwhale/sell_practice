package org.zxy.sell.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zxy.sell.dataobject.ProductCategory;

import java.util.Arrays;
import java.util.Set;

@SpringBootTest
@Slf4j
class ProductCategoryRepositoryTest {

    @Autowired
    private ProductCategoryRepository repository;

    @Test
    void test() {
        ProductCategory productCategory = repository.findById(1).orElse(null);
        log.info("查询到的数据为： {}", productCategory);
    }

    @Test
    void save() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName("女生最爱");
        productCategory.setCategoryType(4);
        ProductCategory save = repository.save(productCategory);
        log.info("添加结果：{}", save);
    }

    @Test
    void findByCategoryTypeIn() {
        Set<Integer> productCategoryIdList = Sets.newHashSet(Arrays.asList(1, 2, 3, 4));
        Set<ProductCategory> result_list = repository.findByCategoryTypeIn(productCategoryIdList);
        log.info("查询结果是:{}", result_list);
    }

}