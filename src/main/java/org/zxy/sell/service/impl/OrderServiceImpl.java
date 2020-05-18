package org.zxy.sell.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zxy.sell.dataobject.OrderDetail;
import org.zxy.sell.dataobject.OrderMaster;
import org.zxy.sell.dataobject.ProductInfo;
import org.zxy.sell.dto.CartDTO;
import org.zxy.sell.dto.OrderDTO;
import org.zxy.sell.enums.ExceptionEnum;
import org.zxy.sell.enums.PayStatusEnum;
import org.zxy.sell.enums.ProductStatusEnum;
import org.zxy.sell.exception.SellException;
import org.zxy.sell.repository.OrderDetailRepository;
import org.zxy.sell.repository.OrderMasterRepository;
import org.zxy.sell.service.IOrderService;
import org.zxy.sell.utils.KeyUtil;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductServiceImpl productService;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {

        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);

        String orderId = KeyUtil.getUniqueKey();

        // 1. 查询商品，数量，价格；
        List<String> productIdList = orderDTO.getOrderDetailList().stream()
                .map(OrderDetail::getProductId)
                .collect(Collectors.toList());

        List<ProductInfo> productInfoList = productService.findByProductIdInAndProductStatus(productIdList,
                ProductStatusEnum.ON_SALE.getCode());

        Map<String, ProductInfo> productInfoMap =
                productInfoList.stream()
                        .collect((Collectors.toMap(ProductInfo::getProductId, productInfo -> productInfo)));

        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            ProductInfo productInfo = productInfoMap.get(orderDetail.getProductId());
            if (productInfo == null) {
                throw new SellException(ExceptionEnum.PRODUCT_NOT_EXIST);
            }

            // 2. 计算总价；
            orderAmount = productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);

            orderDetail.setOrderId(orderId);
            orderDetail.setDetailId(KeyUtil.getUniqueKey());
            BeanUtils.copyProperties(productInfo, orderDetail);
        }

        // 3. 写入订单数据库:OrderMaster OrderDetail;
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderId(orderId);
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        List<OrderDetail> orderDetailList = orderDetailRepository.saveAll(orderDTO.getOrderDetailList());
        if (orderDetailList == null) {
            throw new SellException(ExceptionEnum.ERROR);
        }

        OrderMaster orderMasterResult = orderMasterRepository.save(orderMaster);
        if (orderMasterResult == null) {
            throw new SellException(ExceptionEnum.ERROR);
        }

        // 4. 扣库存；
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e -> {
                    CartDTO cartDTO = new CartDTO();
                    BeanUtils.copyProperties(e, cartDTO);
                    return cartDTO;
                }).collect(Collectors.toList());
        productService.decreaseStock(cartDTOList);
        return null;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        return null;
    }

    @Override
    public Page<OrderMaster> findList(String buyerOpenid, Pageable pageable) {
        return null;
    }

    @Override
    public OrderDTO cancel(OrderDTO orderDTO) {
        return null;
    }

    @Override
    public OrderDTO finished(OrderDTO orderDTO) {
        return null;
    }

    @Override
    public OrderDTO paid(OrderDTO orderDTO) {
        return null;
    }
}
