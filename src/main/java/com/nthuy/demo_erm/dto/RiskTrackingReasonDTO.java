package com.nthuy.demo_erm.dto;

import com.nthuy.demo_erm.dto.response.RiskResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskTrackingReasonDTO {
    private Long id;
    private TrackingReasonDTO trackingReason;
    private SampleActionDTO sampleActionDTO;
    private List<SampleActionDTO> sampleAction;
}

