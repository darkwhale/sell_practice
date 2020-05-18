package org.zxy.sell.enums;

import lombok.Getter;

@Getter
public enum ExceptionEnum {

    ERROR(-1, "服务器错误"),

    PRODUCT_NOT_EXIST(10, "商品已下架或不存在"),

    PRODUCT_STOCK_NOT_ENOUGH(11, "商品库存不足"),
    ;

    private Integer code;

    private String msg;

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
