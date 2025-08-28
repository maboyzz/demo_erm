package com.nthuy.demo_erm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "classify_reason_map",
        uniqueConstraints = @UniqueConstraint(columnNames = {"classify_reason_id","system_id"}))
public class ClassifyReasonMapEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "classify_reason_id")
    private Long classifyReasonId;
    @Column(name = "system_id")
    private Long systemId;
}
