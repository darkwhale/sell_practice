package org.zxy.sell.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zxy.sell.dataobject.ProductCategory;
import org.zxy.sell.repository.ProductCategoryRepository;
import org.zxy.sell.service.ICategoryService;

import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private ProductCategoryRepository repository;

    @Override
    public ProductCategory findOneById(Integer categoryId) {
        return repository.findById(categoryId).orElse(null);
    }

    @Override
    public List<ProductCategory> findAll() {
        return repository.findAll();
    }

    @Override
    public Set<ProductCategory> findByCategoryTypeIn(Set<Integer> categoryTypeList) {
        return repository.findByCategoryTypeIn(categoryTypeList);
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return repository.save(productCategory);
    }
}
