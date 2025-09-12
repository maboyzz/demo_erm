package com.nthuy.demo_erm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "tracking_action_map",
        uniqueConstraints = @UniqueConstraint(columnNames = {"tracking_action_id","department_id"}))
public class TrackingActionMapEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "tracking_action_id")
    private Long trackingActionId;
    @Column(name = "department_id")
    private Long departmentId;
}
