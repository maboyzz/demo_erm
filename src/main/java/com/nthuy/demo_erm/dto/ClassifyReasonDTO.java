package com.nthuy.demo_erm.dto;

import lombok.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassifyReasonDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String note;
    private Set<SystemDTO> systems; // <- thuộc tính mà mapper sẽ map vào
}