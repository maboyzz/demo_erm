package com.nthuy.demo_erm.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributeValueDTO {
    private Long id;
    private String value;
    private Long attributeId; // sẽ được service set (client có thể bỏ)
}