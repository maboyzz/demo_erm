package com.nthuy.demo_erm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "sample_action_line_map",
        uniqueConstraints = @UniqueConstraint(columnNames = {"sample_action_line_id","department_id"}))
public class SampleActionLineMapEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "sample_action_line_id")
    private Long sampleActionLineId;
    @Column(name = "department_id")
    private Long departmentId;
}
