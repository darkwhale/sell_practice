package org.zxy.sell.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.data.domain.Pageable;
import org.zxy.sell.converter.OrderMaster2OrderDTO;
import org.zxy.sell.dataobject.OrderDetail;
import org.zxy.sell.dataobject.OrderMaster;
import org.zxy.sell.dataobject.ProductInfo;
import org.zxy.sell.dto.CartDTO;
import org.zxy.sell.dto.OrderDTO;
import org.zxy.sell.enums.ExceptionEnum;
import org.zxy.sell.enums.OrderStatusEnum;
import org.zxy.sell.enums.PayStatusEnum;
import org.zxy.sell.enums.ProductStatusEnum;
import org.zxy.sell.exception.SellException;
import org.zxy.sell.repository.OrderDetailRepository;
import org.zxy.sell.repository.OrderMasterRepository;
import org.zxy.sell.service.IOrderService;
import org.zxy.sell.utils.KeyUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
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

            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetail.setOrderId(orderId);
            orderDetail.setDetailId(KeyUtil.getUniqueKey());
        }

        // 3. 写入订单数据库:OrderMaster OrderDetail;
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        orderDTO.setOrderAmount(orderAmount);
        BeanUtils.copyProperties(orderDTO, orderMaster);
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
        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        OrderMaster orderMaster = orderMasterRepository.findById(orderId).orElse(null);
        if (orderMaster == null) {
            throw new SellException(ExceptionEnum.ORDER_NOT_EXIST);
        }

        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)) {
            throw new SellException(ExceptionEnum.ORDER_DETAIL_NOT_EXIST);
        }

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {

        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);

        List<OrderDTO> orderDTOList = OrderMaster2OrderDTO.convert(orderMasterPage.getContent());

        return new PageImpl<>(orderDTOList, pageable, orderMasterPage.getTotalElements());
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        // 判断订单状态;
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("[取消订单], 订单状态不正确,orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ExceptionEnum.ORDER_STATUS_ERROR);
        }

        // 修改订单状态；
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster);

        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("[取消订单]， orderMaster={}", orderMaster);
            throw new SellException(ExceptionEnum.ORDER_UPDATE_ERROR);
        }

        // 返还库存；
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("[取消订单], 订单中无商品: orderDTO={}", orderDTO);
            throw new SellException(ExceptionEnum.ORDER_DETAIL_NOT_EXIST);
        }

        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity())).collect(Collectors.toList());
        productService.increaseStock(cartDTOList);


        // 如果已支付，需要退款
        if (orderDTO.getPayStatus().equals(PayStatusEnum.PAID.getCode())) {
            // todo 退款
        }

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finished(OrderDTO orderDTO) {
        // 判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("[完结订单]， 订单状态错误， orderDto={}", orderDTO);
            throw new SellException(ExceptionEnum.ORDER_STATUS_ERROR);
        }

        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("[完结订单]， 支付状态错误， orderDto={}", orderDTO);
            throw new SellException(ExceptionEnum.PAY_STATUS_ERROR);
        }

        // 修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster result = orderMasterRepository.save(orderMaster);
        if(result == null) {
            log.error("[完结订单]， 更新失败， orderMaster={}", orderMaster);
            throw new SellException(ExceptionEnum.ERROR);
        }
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        // 判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("[订单支付完成]， 订单状态错误， orderDto={}", orderDTO);
            throw new SellException(ExceptionEnum.ORDER_STATUS_ERROR);
        }

        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("[订单支付完成]， 支付状态错误， orderDto={}", orderDTO);
            throw new SellException(ExceptionEnum.PAY_STATUS_ERROR);
        }

        // 修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.PAID.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster result = orderMasterRepository.save(orderMaster);
        if(result == null) {
            log.error("[订单支付完成]， 更新失败， orderMaster={}", orderMaster);
            throw new SellException(ExceptionEnum.ERROR);
        }

        return orderDTO;
    }

}
