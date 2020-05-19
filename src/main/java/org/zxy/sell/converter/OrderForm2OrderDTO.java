package org.zxy.sell.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.zxy.sell.dataobject.OrderDetail;
import org.zxy.sell.dto.OrderDTO;
import org.zxy.sell.enums.ExceptionEnum;
import org.zxy.sell.exception.SellException;
import org.zxy.sell.form.OrderForm;

import java.util.List;

@Slf4j
public class OrderForm2OrderDTO {

    private static Gson gson = new Gson();

    public static OrderDTO convert(OrderForm orderForm) {
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());

        List<OrderDetail> orderDetailList;
        try {
            orderDetailList = gson.fromJson(orderForm.getItems(), new TypeToken<List<OrderDetail>>(){}.getType());
        }catch (Exception e) {
            log.error("[对象转换], 错误, string={}", orderForm.getItems());
            throw new SellException(ExceptionEnum.PARAM_ERROR);
        }

        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;
    }
}
