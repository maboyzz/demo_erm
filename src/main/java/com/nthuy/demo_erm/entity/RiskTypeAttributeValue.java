package com.nthuy.demo_erm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "attribute_risk_type_value",
        uniqueConstraints = @UniqueConstraint(columnNames = {"attribute_risk_type_id","attribute_value_id"}))
public class RiskTypeAttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "attribute_risk_type_id")
    private Long riskTypeAttributeId;
    @Column(name = "attribute_value_id")
    private Long attributeValueId;
}
