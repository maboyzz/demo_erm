package com.nthuy.demo_erm.dto;

import com.nthuy.demo_erm.dto.response.RiskResponse;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskTrackingReasonDTO {
    private Long id;
    private TrackingReasonDTO trackingReason;

}

