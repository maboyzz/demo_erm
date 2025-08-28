package com.nthuy.demo_erm.dto;

import com.nthuy.demo_erm.common.constant.EnumOriginReason;
import com.nthuy.demo_erm.common.constant.EnumTypeReason;
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
    @NotEmpty(message = "code không được để trống")
    private String name;
    private EnumTypeReason type;
    private Long classifyReasonId;
    private EnumOriginReason origin;
    private String note;
    private boolean active;
    private Set<SystemDTO> systems;
}
