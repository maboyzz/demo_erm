package com.nthuy.demo_erm.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnumState {
    NOT_STARTED("Chưa thực hiện"),
    COMPLETED("Hoàn thành"),
    IN_PROGRESS("Đang thực hiện");

    private final String description;
}
