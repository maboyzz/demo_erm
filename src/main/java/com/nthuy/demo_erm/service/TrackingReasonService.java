package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.TagDTO;
import com.nthuy.demo_erm.dto.TrackingReasonDTO;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface TrackingReasonService {
    Long create(TrackingReasonDTO sampleDto);

    TrackingReasonDTO getTrackingReason(Long id);

    void delete(Long id);

    Long update(TrackingReasonDTO dto) throws NameExisted;

    ResultPaginationDTO<TrackingReasonDTO> getListTrackingReason(Set<Long> ids, Pageable pageable);

}
