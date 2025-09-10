package com.nthuy.demo_erm.dto;

import com.nthuy.demo_erm.common.constant.EnumObjectApplicableType;
import com.nthuy.demo_erm.common.constant.EnumState;
import com.nthuy.demo_erm.dto.response.ClassifyReasonResponse;
import com.nthuy.demo_erm.dto.response.ReasonResponse;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackingReasonDTO {
    private Long id;
    private ClassifyReasonResponse classifyReason;
    private ReasonResponse reason;
    private EnumObjectApplicableType objectApplicableType;
    private int count;
    private EnumState state;
}
