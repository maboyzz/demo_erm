package com.nthuy.demo_erm.dto;

import com.nthuy.demo_erm.common.constant.EnumPriorityLevel;
import com.nthuy.demo_erm.dto.response.IdCodeNameResponse;
import com.nthuy.demo_erm.dto.response.RiskCategoryResponse;
import com.nthuy.demo_erm.dto.response.RiskTypeResponse;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskDTO {
    private Long id;
    @NotEmpty(message = "code không được để trống")
    private String code;
    @NotEmpty(message = "name không được để trống")
    private String name;
    @NotNull(message = "system không được để trống")
    private SystemDTO system;
    @NotNull(message = "riskType không được để trống")
    private IdCodeNameResponse riskType;
    private IdCodeNameResponse riskCategory;
    //@NotEmpty(message = "reporter không được để trống")
    private EmployeeDTO reporter;
    private Timestamp recognitionTime;
    @NotNull(message = "priorityLevel không được để trống")
    private EnumPriorityLevel priorityLevel;
    @NotEmpty(message = "description không được để trống")
    private String description;
    private String expectedConsequences;
    private int level;
    private int point;
    private List<RiskLineDTO> riskLine;
    private Set<TagDTO> tags;
    private List<RiskFileDTO> riskFile;
    private Set<TrackingReasonDTO> trackingReason;
    private Set<RiskRelationDTO> relations;
}
