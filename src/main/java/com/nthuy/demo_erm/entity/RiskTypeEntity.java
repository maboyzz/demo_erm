package com.nthuy.demo_erm.entity;

import com.nthuy.demo_erm.common.constant.EnumObject;
import com.nthuy.demo_erm.common.constant.EnumOrigin;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "risk_type")
@ToString
public class RiskTypeEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String code;
    @Column(unique = true, nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "risk_origin")
    private EnumOrigin origin;
    private String note;
    private EnumObject object;
    private boolean isActive;

}
