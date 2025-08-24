package com.nthuy.demo_erm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "system")
public class SystemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String name;

    // Quan hệ ngược với ClassifyReasonEntity
    @ManyToMany(mappedBy = "systemEntities", fetch = FetchType.LAZY)
    private Set<ClassifyReasonEntity> classifyReasons = new HashSet<>();

    @Override
    public String toString() {
        return "SystemEntity{" +
                "id=" + id +
                ", name='" + name;
    }
}