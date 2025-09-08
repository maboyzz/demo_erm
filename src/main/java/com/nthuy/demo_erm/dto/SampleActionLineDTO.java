package com.nthuy.demo_erm.dto;

import com.nthuy.demo_erm.common.constant.EnumActionType;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SampleActionLineDTO {
    private Long id;
    private String code;
    private String name;
    private EnumActionType actionType;
    private String content;
    private Set<DepartmentDTO> departments;
}
