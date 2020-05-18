package org.zxy.sell.enums;

import lombok.Getter;

@Getter
public enum ProductStatusEnum {

    ON_SALE(0, "在售"),

    OFF_SALE(1, "下架"),
    ;

    private Integer code;

    private String msg;

    ProductStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
