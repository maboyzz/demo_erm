package com.nthuy.demo_erm.common.constant;

public enum EnumTypeAttributeGroup {
    SYSTEM("hệ thống"),
    BUSINESS("tự tạo");

    private final String description;


    EnumTypeAttributeGroup(String description) {
        this.description = description;
    }
}
