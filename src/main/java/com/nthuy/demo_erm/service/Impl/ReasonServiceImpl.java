package com.nthuy.demo_erm.service.Impl;


import com.nthuy.demo_erm.config.ReasonSpecification;
import com.nthuy.demo_erm.constant.EnumTypeReason;
import com.nthuy.demo_erm.dto.Meta;
import com.nthuy.demo_erm.dto.ReasonDTO;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.entity.ReasonEntity;
import com.nthuy.demo_erm.entity.SystemEntity;
import com.nthuy.demo_erm.exception.BadRequestValidationException;
import com.nthuy.demo_erm.exception.IdInvalidException;
import com.nthuy.demo_erm.exception.NameExisted;
import com.nthuy.demo_erm.mapper.ReasonMapper;
import com.nthuy.demo_erm.repository.ReasonRepository;
import com.nthuy.demo_erm.repository.SystemRepository;
import com.nthuy.demo_erm.service.ReasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReasonServiceImpl implements ReasonService {

    private final ReasonRepository reasonRepository;
    private final ReasonMapper reasonMapper;
    private final SystemRepository systemRepository;


    private boolean nameExists(String userName) {
        return this.reasonRepository.existsByName(userName);
    }

    private boolean existsById(Long id) {
        return this.reasonRepository.existsById(id);
    }

    @Override
    public Long handleCreateClassifyReason(ReasonDTO dto) throws NameExisted {
        if (this.nameExists(dto.getName())) {
            throw new NameExisted("Name đã có");
        }
        ReasonEntity entity = reasonMapper.toEntity(dto);
        Set<SystemEntity> systemEntities;
        if (dto.getSystems() != null && !dto.getSystems().isEmpty()) {
            // Nếu DTO có truyền systems thì lấy theo danh sách đó
            systemEntities = dto.getSystems().stream().map(systemDTO -> systemRepository.findById(systemDTO.getId()).orElseThrow(() -> new RuntimeException("System not found with id: " + systemDTO.getId()))).collect(Collectors.toSet());
        } else {
            // Nếu không truyền thì mặc định lấy system có id = 1 và id = 2
            systemEntities = new HashSet<>(systemRepository.findAllById(Arrays.asList(1L, 2L)));
        }
        entity.setSystemEntitiesReason(systemEntities);
        System.out.println("Entity trước khi lưu: " + entity);
        ReasonEntity savedEntity = reasonRepository.save(entity);

        return savedEntity.getId();
    }

    @Override
    public ReasonDTO handleGetReasonById(Long id) {

        ReasonEntity reason = this.reasonRepository.findById(id).orElseThrow(() -> new BadRequestValidationException("Thẻ bảo hiểm với ID " + id + " không tồn tại"));
        return reasonMapper.toDto(reason);

    }

    @Override
    public void handleDeleteReason(Long id) {
        if (this.existsById(id)) {
            throw new IdInvalidException("Id không có" + id);
        }
        this.reasonRepository.deleteById(id);
    }

    @Override
    public Long handleUpdateReason(ReasonDTO dto) throws NameExisted {
        // Lấy entity cũ từ DB
        if (this.nameExists(dto.getName())) {
            throw new NameExisted("Name đã có");
        }
        ReasonEntity reason = reasonRepository.findById(dto.getId()).orElseThrow(() -> new BadRequestValidationException("Phân loại nguyên nhân với ID " + dto.getId() + " không tồn tại"));
        reasonMapper.updateEntityFromDto(dto, reason);
        Set<SystemEntity> systemEntities;

        if (dto.getSystems() != null && !dto.getSystems().isEmpty()) {
            // Nếu DTO có truyền systems thì lấy theo danh sách đó
            systemEntities = dto.getSystems().stream().map(systemDTO -> systemRepository.findById(systemDTO.getId()).orElseThrow(() -> new RuntimeException("System not found with id: " + systemDTO.getId()))).collect(Collectors.toSet());
        } else {
            // Nếu không truyền thì mặc định lấy system có id = 1 và id = 2
            systemEntities = new HashSet<>(systemRepository.findAllById(Arrays.asList(1L, 2L)));
        }
        reason.setSystemEntitiesReason(systemEntities);

        return reasonRepository.save(reason).getId();
    }

    @Override
    public ResultPaginationDTO<ReasonDTO> handleGetReason(String code, String name, List<Long> systemIds, Boolean isActive, EnumTypeReason type, Pageable pageable) {

        Specification<ReasonEntity> spec = Specification.where(ReasonSpecification.hasCode(code)).and(ReasonSpecification.hasName(name)).and(ReasonSpecification.hasSystemIdIn(systemIds)).and(ReasonSpecification.hasType(type)).and(ReasonSpecification.hasIsActive(isActive));

        Page<ReasonEntity> pageResult = reasonRepository.findAll(spec, pageable);

        List<ReasonDTO> dtoList = reasonMapper.toDtoList(pageResult.getContent());

        Meta meta = new Meta();
        meta.setPage(pageResult.getNumber());
        meta.setSize(pageResult.getSize());
        meta.setTotalElements(pageResult.getTotalElements());
        meta.setTotalPages(pageResult.getTotalPages());
        meta.setNumberOfElements(pageResult.getNumberOfElements());
        meta.setSort(pageable.getSort().toString());

        ResultPaginationDTO<ReasonDTO> result = new ResultPaginationDTO<>();
        result.setContent(dtoList);
        result.setMeta(meta);

        return result;
    }

}
