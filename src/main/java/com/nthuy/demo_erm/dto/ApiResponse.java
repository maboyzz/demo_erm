package com.nthuy.demo_erm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private String message;
    private String traceId;
    private T data;
}