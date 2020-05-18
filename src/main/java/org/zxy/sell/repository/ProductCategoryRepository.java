package org.zxy.sell.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zxy.sell.dataobject.ProductCategory;

import java.util.Set;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

    Set<ProductCategory> findByCategoryTypeIn(Set<Integer> categoryTypeList);
}
