package com.nthuy.demo_erm.entity;

import com.nthuy.demo_erm.common.constant.EnumActionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sample_action_line")
@ToString
public class SampleActionLineEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long handlingMeasureId;
    private Long sampleActionId;
    @Enumerated(EnumType.STRING)
    private EnumActionType actionType;
    private String content;
}
