package com.nthuy.demo_erm.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnumObject {
    B2B_CUSTOMER("Khách hàng B2B"),
    B2C_CUSTOMER("Khách hàng B2C"),
    OEM_CUSTOMER("Khách hàng OEM"),
    SUPPLIER("Nhà cung cấp"),
    SHIPPING_PARTNER("Đối tác vận chuyển"),
    PAYMENT_PARTNER("Đối tác thanh toán"),
    OTHER_PARTNER("Đối tác khác"),
    MERCHANT("Merchant"),
    COMPETITOR("Đối thủ"),
    INTERNAL_PARTNER("Đối tác nội bộ"),

    CORPORATION("Tập đoàn"),
    ORGANIZATION("Tổ chức"),
    DEPARTMENT("Bộ phận"),
    INDIVIDUAL("Cá nhân");

    private final String description;

}
