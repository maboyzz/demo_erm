package com.nthuy.demo_erm.repository;

import com.nthuy.demo_erm.entity.ClassifyReasonMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassifyReasonMapRepository extends JpaRepository<ClassifyReasonMapEntity, Long> {
    // Có thể thêm methods tìm kiếm nếu cần
    List<ClassifyReasonMapEntity> findByClassifyReasonId(Long classifyReasonId);

    void deleteByClassifyReasonId(Long classifyReasonId);
    List<ClassifyReasonMapEntity> findByClassifyReasonIdIn(List<Long> reasonIds);
}