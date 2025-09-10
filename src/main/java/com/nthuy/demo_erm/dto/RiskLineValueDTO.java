package com.nthuy.demo_erm.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskLineValueDTO {
    private Long id;
    private AttributeValueDTO attributeValue;
    private String textValue;
}

