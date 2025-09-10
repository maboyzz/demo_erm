

package com.nthuy.demo_erm.entity;

import com.nthuy.demo_erm.common.constant.EnumPriorityLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "risk")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String code;
    @Column(unique = true, nullable = false)
    private String name;
    private Long systemId;
    private Long riskTypeId;
    private Long riskCategoryId;
    private Long reporterId;
    private Timestamp recognitionTime;
    @Enumerated(EnumType.STRING)
    private EnumPriorityLevel priorityLevel;
    private String description;
    private String expectedConsequences;
    private int level;
    private int point;
}
