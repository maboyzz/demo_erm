package com.nthuy.demo_erm.repository;



import com.nthuy.demo_erm.entity.ReasonMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ReasonMapRepository extends JpaRepository<ReasonMapEntity, Long> {
    List<ReasonMapEntity> findByReasonId(Long reasonId);

    void deleteByReasonId(Long reasonId);
    List<ReasonMapEntity> findByReasonIdIn(Set<Long> reasonIds);
}
