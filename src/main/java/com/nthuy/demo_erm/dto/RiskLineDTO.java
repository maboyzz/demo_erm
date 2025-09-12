package com.nthuy.demo_erm.dto;


import com.nthuy.demo_erm.dto.response.AttributeResponse;
import com.nthuy.demo_erm.dto.response.IdCodeNameResponse;
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
    private IdCodeNameResponse attributeGroup;
    private List<RiskLineValueDTO> lineValues;
}
