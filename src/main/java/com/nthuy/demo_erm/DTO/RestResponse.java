package com.nthuy.demo_erm.DTO;

import com.nthuy.demo_erm.Constant.EnumErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestResponse<T> {
    private int statusCode;
    private EnumErrorCode errorCode;
    private Object message;
    private T data;
}