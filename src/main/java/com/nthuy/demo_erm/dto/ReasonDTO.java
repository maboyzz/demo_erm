package com.nthuy.demo_erm.dto;

import com.nthuy.demo_erm.common.constant.EnumOrigin;
import com.nthuy.demo_erm.common.constant.EnumTypeReason;
import com.nthuy.demo_erm.dto.response.ClassifyReasonResponse;
import com.nthuy.demo_erm.dto.response.IdCodeNameResponse;
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
    private EnumOrigin origin;
    private String note;
    private boolean active;
    private IdCodeNameResponse classifyReason;
    private Set<SystemDTO> systems;

}
