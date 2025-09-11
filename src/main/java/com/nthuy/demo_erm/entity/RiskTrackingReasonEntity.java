package com.nthuy.demo_erm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "risk_tracking_reason",
        uniqueConstraints = @UniqueConstraint(columnNames = {"risk_id","tracking_reason_id", "sample_action_id"}))
public class RiskTrackingReasonEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "risk_id")
    private Long riskId;
    @Column(name = "tracking_reason_id")
    private Long trackingReasonId;
    @Column(name = "sample_action_id")
    private Long sampleActionId;
}
