package com.nthuy.demo_erm.service.dto;

import com.nthuy.demo_erm.dto.SystemDTO;
import com.nthuy.demo_erm.entity.ClassifyReasonEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Builder
@Getter
@Setter
public class ReasonData {
    private Map<Long, ClassifyReasonEntity> classifyReasonEntityMap;
    private Map<Long, Set<SystemDTO>> reasonSystemsMap;
}
