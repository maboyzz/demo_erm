package com.nthuy.demo_erm.service.impl;

import com.nthuy.demo_erm.common.exception.IdInvalidException;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.TrackingReasonDTO;
import com.nthuy.demo_erm.mapper.TrackingReasonMapper;
import com.nthuy.demo_erm.repository.TrackingReasonRepository;
import com.nthuy.demo_erm.service.TrackingReasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackingReasonServiceImpl implements TrackingReasonService {
    private final TrackingReasonRepository trackingReasonRepository;
    private final TrackingReasonMapper trackingReasonMapper;


    @Override
    public Long create(TrackingReasonDTO dto) throws NameExisted {
        return trackingReasonMapper.toEntity(dto).getId();
    }

    @Override
    public TrackingReasonDTO getTrackingReason(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Long update(TrackingReasonDTO dto) throws NameExisted {
        return 0L;
    }

    @Override
    public ResultPaginationDTO<TrackingReasonDTO> getListTrackingReason(String code, String name, Pageable pageable) {
        return null;
    }
    //---hepper

    // Check tồn tại ID
    private void validateIdExists(Long id) {
        if (!trackingReasonRepository.existsById(id)) {
            throw new IdInvalidException("Id không tồn tại: " + id);
        }
    }
}
