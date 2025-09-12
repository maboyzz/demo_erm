package com.nthuy.demo_erm.service.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
public class SearchAttribute {
    String code;
    String name;
    Boolean isActive;
    Long attributeGroupId;
}
