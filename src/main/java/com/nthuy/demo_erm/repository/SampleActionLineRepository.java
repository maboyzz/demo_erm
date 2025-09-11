package com.nthuy.demo_erm.repository;

import com.nthuy.demo_erm.entity.AttributeRiskTypeEntity;
import com.nthuy.demo_erm.entity.SampleActionLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SampleActionLineRepository extends JpaRepository<SampleActionLineEntity,Long> {
    List<SampleActionLineEntity> findBySampleActionId(Long sampleActionId);
    void deleteBySampleActionId(Long sampleActionId);

    List<SampleActionLineEntity> findBySampleActionIdIn(Collection<Long> ids);


}
