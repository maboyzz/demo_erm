package com.nthuy.demo_erm.dto;

import com.nthuy.demo_erm.constant.EnumAttributeDataType;
import com.nthuy.demo_erm.constant.EnumAttributeDisplayType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributeDTO {
    private Long id;
    private String code;
    private String name;
    private EnumAttributeDisplayType displayType;
    private EnumAttributeDataType dataType;
    private Long attributeGroupId;
    private String description;
    private boolean active;

    // Khi client muốn response kèm values, service sẽ set trường này
    private List<AttributeValueDTO> values;

}