package com.nthuy.demo_erm.service.dto;

import com.nthuy.demo_erm.common.constant.EnumTypeReason;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class SearchRequest {
    private String code;
    private String name;
    private Boolean isActive;
    private Long attributeGroupId;
    private List<Long> system;
    private EnumTypeReason type;
}
