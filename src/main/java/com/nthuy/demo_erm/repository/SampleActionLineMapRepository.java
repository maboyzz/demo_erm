package com.nthuy.demo_erm.repository;


import com.nthuy.demo_erm.entity.SampleActionLineMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SampleActionLineMapRepository extends JpaRepository<SampleActionLineMapEntity,Long> {
    List<SampleActionLineMapEntity> findBySampleActionLineId(Long riskTypeId);

    void deleteBySampleActionLineId(Long riskTypeId);
    List<SampleActionLineMapEntity> findBySampleActionLineIdIn(List<Long> sampleActionLineId);
}
