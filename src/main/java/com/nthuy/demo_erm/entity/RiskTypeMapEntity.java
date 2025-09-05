package com.nthuy.demo_erm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "risk_type_map",
        uniqueConstraints = @UniqueConstraint(columnNames = {"risk_type_id","system_id"}))
public class RiskTypeMapEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "risk_type_id")
    private Long riskTypeId;
    @Column(name = "system_id")
    private Long systemId;
}
