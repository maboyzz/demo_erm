package com.nthuy.demo_erm.dto.response;

import com.nthuy.demo_erm.common.constant.EnumAttributeDataType;
import com.nthuy.demo_erm.common.constant.EnumAttributeDisplayType;
import com.nthuy.demo_erm.dto.AttributeValueDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributeResponse {
    private Long id;
    private String code;
    private String name;
    private EnumAttributeDisplayType displayType;
    private EnumAttributeDataType dataType;
    private String description;
    private boolean active;

    // Khi client muốn response kèm values, service sẽ set trường này
    private List<AttributeValueDTO> values;
}
