package com.nthuy.demo_erm.dto;

import com.nthuy.demo_erm.common.constant.EnumColor;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagDTO {
    private Long id;
    @NotEmpty(message = "name không để trống")
    private String name;
    @NotNull(message = "color không để trống")
    private EnumColor color;
}
