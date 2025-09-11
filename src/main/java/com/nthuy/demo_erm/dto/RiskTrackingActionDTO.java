package com.nthuy.demo_erm.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskTrackingActionDTO {
    private Long id;
    private RiskTrackingReasonDTO riskTrackingReason;
    private TrackingActionDTO trackingAction;
}
