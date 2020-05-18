package org.zxy.sell.service;

import org.zxy.sell.dataobject.ProductCategory;

import java.util.List;
import java.util.Set;

public interface ICategoryService {

    ProductCategory findOneById(Integer categoryId);

    List<ProductCategory> findAll();

    Set<ProductCategory> findByCategoryTypeIn(Set<Integer> categoryTypeList);

    ProductCategory save(ProductCategory productCategory);
}
