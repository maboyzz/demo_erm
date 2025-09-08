package com.nthuy.demo_erm.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskTypeAttributeValueDTO {
    private Long id;
    private AttributeValueDTO attributeValueDTO;
}
