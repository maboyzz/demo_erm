package com.nthuy.demo_erm.dto;

import com.nthuy.demo_erm.constant.EnumOriginReason;
import com.nthuy.demo_erm.constant.EnumTypeReason;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReasonDTO {
    private Long id;
    private String code;
    private String name;
    private EnumTypeReason type;
    private Long classifyReasonId;
    private EnumOriginReason origin;
    private String note;
    private boolean active;
    private Set<SystemDTO> systems;
}
