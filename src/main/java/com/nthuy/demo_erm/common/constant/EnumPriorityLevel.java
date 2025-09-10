package com.nthuy.demo_erm.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EnumPriorityLevel {
    HIGH("Cao"),
    MEDIUM("Trung binh"),
    LOW("thap");


    private final String description;

}
