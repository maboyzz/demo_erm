package com.nthuy.demo_erm.service.dto;

import com.nthuy.demo_erm.entity.AttributeGroupEntity;
import com.nthuy.demo_erm.entity.AttributeValueEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
public class AttributeData {

    private Map<Long, List<AttributeValueEntity>> attributeValueEntityMapAttribute;
    private Map<Long, AttributeGroupEntity> attributeGroupEntityMap;

}
