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
public class SampleActionLineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String code;
    @Column(unique = true, nullable = false)
    private String name;
    private Long sampleActionId;
    @Enumerated(EnumType.STRING)
    private EnumActionType actionType;
    private String content;
}
