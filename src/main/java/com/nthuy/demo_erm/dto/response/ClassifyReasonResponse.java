package com.nthuy.demo_erm.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassifyReasonResponse {
    private Long id;
    private String code;
    private String name;
}
