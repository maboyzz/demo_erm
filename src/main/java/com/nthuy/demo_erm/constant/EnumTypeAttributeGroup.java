package com.nthuy.demo_erm.constant;

public enum EnumTypeAttributeGroup {
    SYSTEM("hệ thống"),
    BUSINESS("tự tạo");

    private final String description;


    EnumTypeAttributeGroup(String description) {
        this.description = description;
    }
}
