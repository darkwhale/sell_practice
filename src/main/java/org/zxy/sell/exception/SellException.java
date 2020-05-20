package org.zxy.sell.exception;

import org.zxy.sell.enums.ExceptionEnum;

public class SellException extends RuntimeException {


    private Integer code;

    public SellException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMsg());

        this.code = exceptionEnum.getCode();
    }

    public SellException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

}
