package com.nthuy.demo_erm.constant;

import lombok.Getter;

@Getter
public enum EnumTypeReason {
    INCIDENT("Sự cố"),
    RISK("Rủi ro");

    private final String description;


    EnumTypeReason(String description) {
        this.description = description;
    }

}
