package com.nthuy.demo_erm.entity;

import com.nthuy.demo_erm.common.constant.EnumOriginReason;
import com.nthuy.demo_erm.common.constant.EnumTypeReason;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reason")
@ToString
public class ReasonEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @Column(unique = true, nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    private EnumTypeReason type;
    private Long classifyReasonId;
    @Enumerated(EnumType.STRING)
    private EnumOriginReason origin;
    private String note;
    private boolean isActive;
}
