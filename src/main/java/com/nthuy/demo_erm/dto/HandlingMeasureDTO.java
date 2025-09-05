package com.nthuy.demo_erm.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HandlingMeasureDTO {
    private Long id;
    @NotEmpty(message = "code không được để trống")
    private String code;
    @NotEmpty(message = "name không được để trống")
    private String name;
    private String description;
    private boolean active;
}
