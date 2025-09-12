package com.nthuy.demo_erm.dto;

import com.nthuy.demo_erm.dto.response.IdCodeNameResponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SampleActionDTO {
    private Long id;
    private String code;
    private String name;
    private IdCodeNameResponse riskType;
    private IdCodeNameResponse classifyReason;
    private String note;
    private boolean active;
    private List<SampleActionLineDTO> sampleActionLines;
}
