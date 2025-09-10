package com.nthuy.demo_erm.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskFileDTO {
    private Long id;
    private String name;
    private String url;
}
