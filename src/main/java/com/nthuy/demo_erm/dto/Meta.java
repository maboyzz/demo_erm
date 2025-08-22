package com.nthuy.demo_erm.dto;

import lombok.Getter;
import lombok.Setter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meta {
    private int page;            // số trang hiện tại
    private int size;            // số record mỗi trang
    private long totalElements;  // tổng số record
    private int totalPages;      // tổng số trang
    private int numberOfElements;// số phần tử trong trang hiện tại
    private String sort;         // ví dụ: createdAt:DESC
}