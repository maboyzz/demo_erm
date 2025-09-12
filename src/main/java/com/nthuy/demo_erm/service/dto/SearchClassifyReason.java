package com.nthuy.demo_erm.service.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Getter
@Setter
public class SearchClassifyReason {
    private String code;
    private String name;
    private List<Long> system;
}
