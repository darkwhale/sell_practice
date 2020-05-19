package org.zxy.sell.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zxy.sell.enums.ExceptionEnum;

@ControllerAdvice
public class RuntimeExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public void notValidExceptionHandler() {
        throw new SellException(ExceptionEnum.PARAM_ERROR);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public void missingParam() {
        throw new SellException(ExceptionEnum.PARAM_ERROR);
    }

}
