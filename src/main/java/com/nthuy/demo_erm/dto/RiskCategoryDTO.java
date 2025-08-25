package com.nthuy.demo_erm.dto;


import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RiskCategoryDTO {
    private Long id;
    private String code;
    private String name;
    private Long parent_id;
    private String description;
    private boolean active;
    private Set<SystemDTO> systems;
}
