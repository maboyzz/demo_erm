package com.nthuy.demo_erm.entity;

import com.nthuy.demo_erm.common.constant.EnumObjectApplicableType;
import com.nthuy.demo_erm.common.constant.EnumState;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tracking_reason")
@ToString
public class TrackingReasonEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long classifyReasonId;
    private Long reasonId;
    private Long sampleActionId;
    private int count;
    private EnumObjectApplicableType objectApplicableType;
    private EnumState state;
}








