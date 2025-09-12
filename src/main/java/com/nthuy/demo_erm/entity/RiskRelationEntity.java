package com.nthuy.demo_erm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "risk_relation",
        uniqueConstraints = @UniqueConstraint(columnNames = {"risk_id","risk_relation_id"}))
public class RiskRelationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "risk_id")
    private Long riskId;
    @Column(name = "system_id")
    private Long riskRelationId;
}
