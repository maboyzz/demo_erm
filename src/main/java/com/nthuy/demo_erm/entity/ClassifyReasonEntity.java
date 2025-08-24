package com.nthuy.demo_erm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "classify_reason")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassifyReasonEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @Column(unique = true, nullable = false)
    private String name;
    private String description;
    private String note;
    // Many-to-many với System
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "classify_reason_map",
            joinColumns = @JoinColumn(name = "classify_reason_id"),
            inverseJoinColumns = @JoinColumn(name = "system_id")
    )
    private Set<SystemEntity> systemEntitiesClassifyReason= new HashSet<>();


}
