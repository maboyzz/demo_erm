package com.nthuy.demo_erm.dto;

import com.nthuy.demo_erm.common.constant.EnumActionType;
import com.nthuy.demo_erm.dto.response.HandlingMeasureResponse;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SampleActionLineDTO {
    private Long id;
    @NotNull(message ="name không được để trống")
    private HandlingMeasureResponse handlingMeasure;
    private EnumActionType actionType;
    private String content;
    private Set<DepartmentDTO> departments;
}
