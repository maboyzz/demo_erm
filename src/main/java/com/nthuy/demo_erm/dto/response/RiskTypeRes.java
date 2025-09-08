package com.nthuy.demo_erm.dto.response;

import com.nthuy.demo_erm.dto.SystemDTO;
import lombok.*;


import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskTypeRes {
    private Long id;
    private String code;
    private String name;
    private Set<SystemDTO> systems;
    private Boolean active;
}
