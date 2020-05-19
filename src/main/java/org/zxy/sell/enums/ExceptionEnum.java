package org.zxy.sell.enums;

import lombok.Getter;

@Getter
public enum ExceptionEnum {

    ERROR(-1, "服务器错误"),

    PARAM_ERROR(1, "参数不正确"),

    PRODUCT_NOT_EXIST(10, "商品已下架或不存在"),

    PRODUCT_STOCK_NOT_ENOUGH(11, "商品库存不足"),

    ORDER_NOT_EXIST(12, "订单不存在"),

    ORDER_DETAIL_NOT_EXIST(13, "订单详情为空"),

    ORDER_STATUS_ERROR(14, "订单状态不正确"),

    ORDER_UPDATE_ERROR(15, "订单更新失败"),

    PAY_STATUS_ERROR(16, "支付状态不正确"),

    CART_EMPTY(17, "购物车为空"),

    OPENID_ERROR(18, "当前用户错误"),

    ;

    private Integer code;

    private String msg;

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
