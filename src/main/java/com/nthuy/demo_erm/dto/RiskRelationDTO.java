package com.nthuy.demo_erm.dto;

import com.nthuy.demo_erm.dto.response.IdCodeNameResponse;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskRelationDTO {
    private Long id;
    private IdCodeNameResponse riskRelation;
}
