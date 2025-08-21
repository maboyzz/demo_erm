package com.nthuy.demo_erm.DTO;

import lombok.Data;

import java.util.Set;

@Data
public class ClassifyReasonDTO {
    private long id;
    private String code;
    private String name;
    private String description;
    private String note;

    // Quan hệ nhiều-nhiều → dùng Set hoặc List
    private Set<SystemDTO> systems;

    @Data
    public static class SystemDTO {
        private Long id;
        private String name;
    }
}