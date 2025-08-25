package com.nthuy.demo_erm.dto;

import com.nthuy.demo_erm.constant.EnumTypeAttributeGroup;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributeGroupDTO {
    private Long id;
    private String code;
    private String name;
    private EnumTypeAttributeGroup type;
    private String description;
    private boolean active;
}
