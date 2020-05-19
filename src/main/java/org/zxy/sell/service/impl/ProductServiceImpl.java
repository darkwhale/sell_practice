package org.zxy.sell.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.zxy.sell.dataobject.ProductInfo;
import org.zxy.sell.dto.CartDTO;
import org.zxy.sell.enums.ExceptionEnum;
import org.zxy.sell.enums.ProductStatusEnum;
import org.zxy.sell.exception.SellException;
import org.zxy.sell.repository.ProductInfoRepository;
import org.zxy.sell.service.IProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductInfoRepository repository;

    @Override
    public ProductInfo findOne(String productId) {
        return repository.findById(productId).orElse(null);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return repository.findByProductStatus(ProductStatusEnum.ON_SALE.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }

    @Override
    public List<ProductInfo> findByProductIdInAndProductStatus(List<String> productIdList, Integer productStatus) {
        return repository.findByProductIdInAndProductStatus(productIdList, productStatus);
    }

    @Override
    public void increaseStock(List<CartDTO> cartDTOList) {
        Map<String, ProductInfo> productInfoMap = getProductInfoMap(cartDTOList);

        List<ProductInfo> result = new ArrayList<>();
        for (CartDTO cartDTO : cartDTOList) {
            ProductInfo productInfo = productInfoMap.get(cartDTO.getProductId());

            if (productInfo == null) {
                throw new SellException(ExceptionEnum.PRODUCT_NOT_EXIST);
            }

            int new_stock = productInfo.getProductStock() + cartDTO.getProductQuantity();
            productInfo.setProductStock(new_stock);

            result.add(productInfo);
        }
        List<ProductInfo> result_list = repository.saveAll(result);
        if (CollectionUtils.isEmpty(result_list)) {
            throw new SellException(ExceptionEnum.ERROR);
        }
    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList) {
        Map<String, ProductInfo> productInfoMap = getProductInfoMap(cartDTOList);

        List<ProductInfo> result = new ArrayList<>();
        for (CartDTO cartDTO : cartDTOList) {
            ProductInfo productInfo = productInfoMap.get(cartDTO.getProductId());

            if (productInfo == null) {
                throw new SellException(ExceptionEnum.PRODUCT_NOT_EXIST);
            }

            int new_stock = productInfo.getProductStock() - cartDTO.getProductQuantity();

            if (new_stock < 0) {
                throw new SellException(ExceptionEnum.PRODUCT_STOCK_NOT_ENOUGH);
            }
            productInfo.setProductStock(new_stock);

            result.add(productInfo);
        }

        List<ProductInfo> result_list = repository.saveAll(result);
        if (CollectionUtils.isEmpty(result_list)) {
            throw new SellException(ExceptionEnum.ERROR);
        }
    }


    private Map<String, ProductInfo> getProductInfoMap(List<CartDTO> cartDTOList) {
        List<String> productId = cartDTOList.stream().map(CartDTO::getProductId).collect(Collectors.toList());

        List<ProductInfo> productInfoList = repository.findByProductIdInAndProductStatus(productId,
                ProductStatusEnum.ON_SALE.getCode());

        return productInfoList.stream()
                .collect(Collectors.toMap(ProductInfo::getProductId, product -> product));
    }
}
