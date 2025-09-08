package com.nthuy.demo_erm.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EnumActionType {
    CORRECTIVE("Khắc phục"),
    IMPROVEMENT("Cải tiến"),
    PREVENTIVE("Phòng ngừa");

    private final String label;
}
