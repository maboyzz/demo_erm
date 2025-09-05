package com.nthuy.demo_erm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "attribute_risk_type",
        uniqueConstraints = @UniqueConstraint(columnNames = {"risk_type_id","attribute_group_id", "attribute_id"}))
public class RiskTypeAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "risk_type_id")
    private Long riskTypeId;
    @Column(name = "attribute_id")
    private Long attributeId;
    @Column(name = "attribute_group_id")
    private Long attributeGroupId;
}
