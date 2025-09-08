package com.nthuy.demo_erm.dto;

import com.nthuy.demo_erm.dto.response.ClassifyReasonResponse;
import com.nthuy.demo_erm.dto.response.RiskTypeResponse;

import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SampleActionDTO {
    private Long id;
    private String code;
    private String name;
    private RiskTypeResponse riskType;
    private ClassifyReasonResponse classifyReason;
    private String note;
    private boolean active;
    private List<SampleActionLineDTO> sampleActionLines;
}
