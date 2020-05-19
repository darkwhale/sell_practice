package org.zxy.sell.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.zxy.sell.VO.ResultVO;
import org.zxy.sell.converter.OrderForm2OrderDTO;
import org.zxy.sell.dto.OrderDTO;
import org.zxy.sell.enums.ExceptionEnum;
import org.zxy.sell.exception.SellException;
import org.zxy.sell.form.OrderForm;
import org.zxy.sell.service.impl.BuyerService;
import org.zxy.sell.service.impl.OrderServiceImpl;
import org.zxy.sell.utils.ResultVOUtil;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private BuyerService buyerService;

    // 创建订单
    @PostMapping("/create")
    public ResultVO create(@Valid @RequestBody OrderForm orderForm) {
        OrderDTO orderDTO = OrderForm2OrderDTO.convert(orderForm);
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("[创建订单], 购物车不能为空:");
            throw new SellException(ExceptionEnum.CART_EMPTY);
        }

        OrderDTO result = orderService.create(orderDTO);

        Map<String, String> map = new HashMap<>();
        map.put("orderId", result.getOrderId());

        return ResultVOUtil.success(map);
    }


    // 订单列表
    @GetMapping("/list")
    public ResultVO list(@RequestParam(value = "openid") String openid,
                         @RequestParam(value = "page", defaultValue = "0") Integer page,
                         @RequestParam(value = "size", defaultValue = "5") Integer size) {

        if (StringUtils.isEmpty(openid)) {
            log.error("[查询订单列表], openid为空");
            throw new SellException(ExceptionEnum.PARAM_ERROR);
        }

        Page<OrderDTO> orderDTOPage = orderService.findList(openid, PageRequest.of(page, size));
        return ResultVOUtil.success(orderDTOPage.getContent());
    }


    // 订单详情
    @GetMapping("/detail")
    public ResultVO detail(@RequestParam("openid") String openid,
                           @RequestParam("orderId") String orderId) {
        OrderDTO result = buyerService.findOrderOne(openid, orderId);
        return ResultVOUtil.success(result);
    }


    // 取消订单
    @PostMapping("/cancel")
    public ResultVO cancel(@RequestParam("openid") String openid,
                           @RequestParam("orderId") String orderId) {
        buyerService.cancelOrder(openid, orderId);
        return ResultVOUtil.success();
    }

}
