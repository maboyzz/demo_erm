package com.nthuy.demo_erm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "reason_map",
        uniqueConstraints = @UniqueConstraint(columnNames = {"reason_id","system_id"}))
public class ReasonMapEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "reason_id")
    private Long reasonId;
    @Column(name = "system_id")
    private Long systemId;
}
