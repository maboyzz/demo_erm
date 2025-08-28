package com.nthuy.demo_erm.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassifyReasonDTO {
    private Long id;
    private String code;
    @NotEmpty
    private String name;
    private String description;
    private String note;
    private Set<SystemDTO> systems;
}