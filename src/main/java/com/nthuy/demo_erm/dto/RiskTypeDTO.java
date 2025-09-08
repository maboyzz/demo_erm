package com.nthuy.demo_erm.dto;

import com.nthuy.demo_erm.common.constant.EnumObject;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskTypeDTO {
    private Long id;
    private String code;
    private String name;
    private String origin;
    private EnumObject object;
    private String note;
    private Boolean active;
    private List<RiskTypeAttributeDTO> groups;
    private Set<SystemDTO> systems;
}
