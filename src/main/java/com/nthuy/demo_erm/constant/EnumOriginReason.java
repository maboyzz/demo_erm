package com.nthuy.demo_erm.constant;

public enum EnumOriginReason {
    INTERNALORIGIN("Nội bộ"),
    EXTERNALORIGIN("Bên Ngoài");


    private final String description;


    EnumOriginReason(String description) {
        this.description = description;
    }

    }
