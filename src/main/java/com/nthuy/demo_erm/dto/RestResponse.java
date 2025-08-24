package com.nthuy.demo_erm.dto;

import com.nthuy.demo_erm.constant.EnumErrorCode;
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