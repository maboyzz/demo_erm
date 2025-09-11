package com.nthuy.demo_erm.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "risk_tracking_action",
        uniqueConstraints = @UniqueConstraint(columnNames = {"risk_tracking_reason_id","tracking_action_id"}))
public class RiskTrackingActionEntity extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "risk_tracking_reason_id")
    private Long riskTrackingReasonId;
    @Column(name = "tracking_action_id")
    private Long trackingActionId;

}
