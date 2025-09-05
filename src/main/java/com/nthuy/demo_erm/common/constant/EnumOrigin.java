package com.nthuy.demo_erm.common.constant;

public enum EnumOrigin {
    INTERNALORIGIN("Nội bộ"),
    EXTERNALORIGIN("Bên Ngoài");


    private final String description;


    EnumOrigin(String description) {
        this.description = description;
    }

    }
