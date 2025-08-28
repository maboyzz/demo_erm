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
}
