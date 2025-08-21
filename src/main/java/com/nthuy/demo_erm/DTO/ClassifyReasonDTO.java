package com.nthuy.demo_erm.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassifyReasonDTO {
    private long id;
    private String code;
    private String name;
    private String description;
    private String note;

    // Quan hệ nhiều-nhiều → dùng Set hoặc List
    private Set<SystemDTO> systems;



}