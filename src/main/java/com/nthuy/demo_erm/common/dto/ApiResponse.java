package com.nthuy.demo_erm.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private String message;
    private String traceId;
    private T data;
}