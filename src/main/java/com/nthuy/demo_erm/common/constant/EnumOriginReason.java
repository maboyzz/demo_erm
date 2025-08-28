package com.nthuy.demo_erm.common.constant;

public enum EnumOriginReason {
    INTERNALORIGIN("Nội bộ"),
    EXTERNALORIGIN("Bên Ngoài");


    private final String description;


    EnumOriginReason(String description) {
        this.description = description;
    }

    }
