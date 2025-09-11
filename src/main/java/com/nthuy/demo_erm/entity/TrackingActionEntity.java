package com.nthuy.demo_erm.entity;

import com.nthuy.demo_erm.common.constant.EnumActionType;
import com.nthuy.demo_erm.dto.SampleActionLineDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "tracking_action")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackingActionEntity extends BaseEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long handlingMeasureId;
    @Enumerated(EnumType.STRING)
    private EnumActionType actionType;
    private String content;
    private Date completionTime;
}
