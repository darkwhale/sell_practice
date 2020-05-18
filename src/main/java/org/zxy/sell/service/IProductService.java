package org.zxy.sell.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zxy.sell.dataobject.ProductInfo;
import org.zxy.sell.dto.CartDTO;

import java.util.List;

public interface IProductService {

    ProductInfo findOne(String productId);

    // 查询所有在售的商品；
    List<ProductInfo> findUpAll();

    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

    List<ProductInfo> findByProductIdInAndProductStatus(List<String> productIdList, Integer productStatus);

    // todo 加库存
    void increaseStock(List<CartDTO> cartDTOList);

    // todo 减库存
    void decreaseStock(List<CartDTO> cartDTOList);
}
