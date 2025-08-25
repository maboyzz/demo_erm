package com.nthuy.demo_erm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "risk_category")
@ToString
public class RiskCategoryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @Column(unique = true, nullable = false)
    private String name;
    private Long parent_id;
    private String description;
    private boolean isActive;

    // Many-to-many với System
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "risk_category_map",
            joinColumns = @JoinColumn(name = "risk_category_id"),
            inverseJoinColumns = @JoinColumn(name = "system_id")
    )
    private Set<SystemEntity> systemEntitiesRiskCategory= new HashSet<>();
}
