package com.nthuy.demo_erm.entity;

import com.nthuy.demo_erm.constant.EnumAttributeDataType;
import com.nthuy.demo_erm.constant.EnumAttributeDisplayType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "attribute")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttributeEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @Column(unique = true, nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    private EnumAttributeDisplayType displayType;
    @Enumerated(EnumType.STRING)
    @Column(name = "data_type")
    private EnumAttributeDataType dataType;
    private Long attributeGroupId;
    private String description;
    private boolean isActive;
}
