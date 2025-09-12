package com.nthuy.demo_erm.repository;


import com.nthuy.demo_erm.entity.TrackingActionMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackingActionMapRepository extends JpaRepository<TrackingActionMapEntity, Long> {
    List<TrackingActionMapEntity> findByTrackingActionId(Long id);
}
