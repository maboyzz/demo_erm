package com.nthuy.demo_erm.dto;

import com.nthuy.demo_erm.common.constant.EnumActionType;
import com.nthuy.demo_erm.dto.response.HandlingMeasureResponse;
import com.nthuy.demo_erm.dto.response.IdCodeNameResponse;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackingActionDTO
{
    private Long id;
    private IdCodeNameResponse handlingMeasure;
    private EnumActionType actionType;
    private String content;
    private Set<DepartmentDTO> departments;
    private Date completionTime;

}
