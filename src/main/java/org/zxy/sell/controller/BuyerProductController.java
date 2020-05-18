package org.zxy.sell.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zxy.sell.VO.ProductInfoVO;
import org.zxy.sell.VO.ProductVO;
import org.zxy.sell.VO.ResultVO;
import org.zxy.sell.dataobject.ProductCategory;
import org.zxy.sell.dataobject.ProductInfo;
import org.zxy.sell.service.impl.CategoryServiceImpl;
import org.zxy.sell.service.impl.ProductServiceImpl;
import org.zxy.sell.utils.ResultVOUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("/list")
    public ResultVO list() {
        // 1. 查询所有上架商品；
        List<ProductInfo> productInfoList = productService.findUpAll();

        // 2. 查询所有类目；

        Set<Integer> categoryTypeSet = productInfoList.stream()
                .map(ProductInfo::getCategoryType)
                .collect(Collectors.toSet());
        Set<ProductCategory> productCategorySet = categoryService.findByCategoryTypeIn(categoryTypeSet);

        Map<Integer, List<ProductInfo>> productCategoryMap = productInfoList
                .stream()
                .collect(Collectors.groupingBy(ProductInfo::getCategoryType, Collectors.toList()));

        List<ProductVO> productVOList = new ArrayList<>();
        for (ProductCategory productCategory : productCategorySet) {
            ProductVO productVO = new ProductVO();
            productVO.setCategoryType(productCategory.getCategoryType());
            productVO.setCategoryName(productCategory.getCategoryName());
            productVO.setProductInfoVOList(productCategoryMap
                    .get(productCategory.getCategoryType())
                    .stream()
                    .map(e -> {
                        ProductInfoVO productInfoVO = new ProductInfoVO();
                        BeanUtils.copyProperties(e, productInfoVO);
                        return productInfoVO;
                    }).collect(Collectors.toList()));

            productVOList.add(productVO);
        }

        return ResultVOUtil.success(productVOList);
    }


}
