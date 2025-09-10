package com.nthuy.demo_erm.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "risk_line",
        uniqueConstraints = @UniqueConstraint(columnNames = {"risk_id","attribute_id","attribute_group_id"}))
public class RiskLineEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "risk_id")
    private Long riskId;
    @Column(name = "attribute_id")
    private Long attributeId;
    @Column(name = "attribute_group_id")
    private Long attributeGroupId;
}
