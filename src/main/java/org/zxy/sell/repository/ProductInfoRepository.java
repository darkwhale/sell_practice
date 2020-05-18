package org.zxy.sell.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zxy.sell.dataobject.ProductInfo;

import java.util.List;

public interface ProductInfoRepository extends JpaRepository<ProductInfo, String> {

    List<ProductInfo> findByProductStatus(Integer productStatus);

    List<ProductInfo> findByProductIdInAndProductStatus(List<String> productIdList, Integer productStatus);
}
