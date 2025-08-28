package com.nthuy.demo_erm.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.nthuy.demo_erm.common.constant.EnumOriginReason;
import com.nthuy.demo_erm.common.constant.EnumTypeReason;
import com.nthuy.demo_erm.dto.response.ClassifyReasonResponse;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReasonDTO {
    private Long id;
    @NotEmpty(message = "code không được để trống")
    private String code;
    @NotEmpty(message = "name không được để trống")
    private String name;
    private EnumTypeReason type;
    private EnumOriginReason origin;
    private String note;
    private boolean active;
    private ClassifyReasonResponse classifyReason;
    private Set<SystemDTO> systems;

}
