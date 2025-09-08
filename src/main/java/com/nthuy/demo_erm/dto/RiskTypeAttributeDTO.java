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
public class RiskTypeAttributeDTO {
    private Long id;
    private AttributeGroupResponse attributeGroup;
    private AttributeResponse attribute;
    private List<RiskTypeAttributeValueDTO> riskTypeAttributeValue;
}
