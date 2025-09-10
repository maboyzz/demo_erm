package com.nthuy.demo_erm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "risk_line_value",
        uniqueConstraints = @UniqueConstraint(columnNames = {"risk_line_id","attribute_value_id"}))
public class RiskLineValueEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "risk_line_id")
    private Long riskLineId;
    @Column(name = "attribute_value_id")
    private Long attributeValueId;
    private String textValue;

}
