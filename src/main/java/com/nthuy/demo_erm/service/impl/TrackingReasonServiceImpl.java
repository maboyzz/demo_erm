package com.nthuy.demo_erm.service.impl;

import com.nthuy.demo_erm.common.exception.IdInvalidException;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.common.until.PaginationUtils;
import com.nthuy.demo_erm.config.TagSpecification;
import com.nthuy.demo_erm.config.TrackingReasonSpecification;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.TagDTO;
import com.nthuy.demo_erm.dto.TrackingReasonDTO;
import com.nthuy.demo_erm.dto.response.AttributeGroupResponse;
import com.nthuy.demo_erm.dto.response.ClassifyReasonResponse;
import com.nthuy.demo_erm.dto.response.IdCodeNameResponse;
import com.nthuy.demo_erm.dto.response.ReasonResponse;
import com.nthuy.demo_erm.entity.TagEntity;
import com.nthuy.demo_erm.entity.TrackingActionEntity;
import com.nthuy.demo_erm.entity.TrackingReasonEntity;
import com.nthuy.demo_erm.mapper.ClassifyReasonMapper;
import com.nthuy.demo_erm.mapper.ReasonMapper;
import com.nthuy.demo_erm.mapper.TrackingReasonMapper;
import com.nthuy.demo_erm.repository.ClassifyReasonRepository;
import com.nthuy.demo_erm.repository.ReasonRepository;
import com.nthuy.demo_erm.repository.TrackingReasonRepository;
import com.nthuy.demo_erm.service.TrackingActionService;
import com.nthuy.demo_erm.service.TrackingReasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TrackingReasonServiceImpl implements TrackingReasonService {
    private final TrackingReasonRepository trackingReasonRepository;
    private final TrackingReasonMapper trackingReasonMapper;
    private final ClassifyReasonRepository classifyReasonRepository;
    private final ReasonRepository reasonRepository;
    private final ReasonMapper reasonMapper;
    private final ClassifyReasonMapper classifyReasonMapper;
    private final TrackingActionService trackingActionService;

    @Override
    public Long create(TrackingReasonDTO dto) {
        TrackingReasonEntity entity = trackingReasonMapper.toEntity(dto);
        return  trackingReasonRepository.save(entity).getId();
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
    public ResultPaginationDTO<TrackingReasonDTO> getListTrackingReason(Set<Long> ids, Pageable pageable) {


        Specification<TrackingReasonEntity> spec = Specification.where(null);

        // thêm điều kiện filter theo ids nếu có
        spec = spec.and(TrackingReasonSpecification.hasIds(ids));

        Page<TrackingReasonEntity> pageResult = trackingReasonRepository.findAll(spec, pageable);

        List<TrackingReasonDTO> dtoList = pageResult.stream().map(entity -> {
            TrackingReasonDTO dto = trackingReasonMapper.toDto(entity);

            // load reason
                Optional.ofNullable(entity.getReasonId()).flatMap(reasonRepository::findById).ifPresent(reason -> dto.setReason(new IdCodeNameResponse(reason.getId(), reason.getCode(), reason.getName())));


            // load classifyReason
            Optional.ofNullable(entity.getClassifyReasonId()).flatMap(classifyReasonRepository::findById).ifPresent(classifyReason -> dto.setClassifyReason(new IdCodeNameResponse(classifyReason.getId(), classifyReason.getCode(), classifyReason.getName())));

            return dto;
        }).toList();

        return PaginationUtils.buildResult(pageResult, dtoList, pageable);
    }

    //---hepper

    // Check tồn tại ID
    private void validateIdExists(Long id) {
        if (!trackingReasonRepository.existsById(id)) {
            throw new IdInvalidException("Id không tồn tại: " + id);
        }
    }
}
