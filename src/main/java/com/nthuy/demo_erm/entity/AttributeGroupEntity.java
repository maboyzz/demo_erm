package com.nthuy.demo_erm.entity;

import com.nthuy.demo_erm.constant.EnumTypeAttributeGroup;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "attribute_group")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttributeGroupEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @Column(unique = true, nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    private EnumTypeAttributeGroup type;
    private String description;
    private boolean isActive;
}
