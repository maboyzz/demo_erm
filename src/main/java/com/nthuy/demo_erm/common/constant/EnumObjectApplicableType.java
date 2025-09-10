package com.nthuy.demo_erm.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter


@RequiredArgsConstructor
public enum EnumObjectApplicableType {
    PROVIDER("Nhà cung cấp"),
    PROCESS("Quy trình"),
    DEVICE("Thiết bị"),
    HUMAN("Con người"),
    PARTNER("Đối tác");

    private final String description;

}
