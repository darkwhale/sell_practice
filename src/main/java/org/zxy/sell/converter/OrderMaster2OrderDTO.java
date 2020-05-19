package org.zxy.sell.converter;

import org.springframework.beans.BeanUtils;
import org.zxy.sell.dataobject.OrderMaster;
import org.zxy.sell.dto.OrderDTO;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMaster2OrderDTO {

    public static OrderDTO convert(OrderMaster orderMaster) {
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        return orderDTO;
    }

    public static List<OrderDTO> convert(List<OrderMaster> orderMasterList) {
        return orderMasterList.stream().map(OrderMaster2OrderDTO::convert).collect(Collectors.toList());
    }
}
