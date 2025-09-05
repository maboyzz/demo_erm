package com.nthuy.demo_erm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "risk_category_map",
        uniqueConstraints = @UniqueConstraint(columnNames = {"risk_category_id","system_id"}))
public class RiskCategoryMapEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "risk_category_id")
    private Long riskCategoryId;
    @Column(name = "system_id")
    private Long systemId;
}
