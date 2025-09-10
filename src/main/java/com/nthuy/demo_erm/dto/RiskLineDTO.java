package com.nthuy.demo_erm.dto;

import com.nthuy.demo_erm.dto.response.AttributeGroupResponse;
import com.nthuy.demo_erm.dto.response.AttributeResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskLineDTO {
    private Long id;
    private AttributeResponse attribute;
    private AttributeGroupResponse attributeGroup;
    private List<RiskLineValueDTO> lineValues;
}
