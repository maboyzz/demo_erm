package com.nthuy.demo_erm.dto;

import com.nthuy.demo_erm.common.constant.EnumObjectApplicableType;
import com.nthuy.demo_erm.common.constant.EnumState;
import com.nthuy.demo_erm.dto.response.IdCodeNameResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackingReasonDTO {
    private Long id;
    private IdCodeNameResponse classifyReason;
    private IdCodeNameResponse reason;
    private EnumObjectApplicableType objectApplicableType;
    private int count;
    private EnumState state;
    private List<TrackingActionDTO> trackingActions;
}
