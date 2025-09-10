package com.nthuy.demo_erm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "risk_tag",
        uniqueConstraints = @UniqueConstraint(columnNames = {"tag_id","risk_id"}))
public class RiskTagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "tag_id")
    private Long tagId;
    @Column(name = "risk_id")
    private Long riskId;
}
