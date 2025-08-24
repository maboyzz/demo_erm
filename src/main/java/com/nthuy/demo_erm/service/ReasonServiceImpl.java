package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.dto.ClassifyReasonDTO;
import com.nthuy.demo_erm.dto.ReasonDTO;
import com.nthuy.demo_erm.entity.ClassifyReasonEntity;
import com.nthuy.demo_erm.entity.ReasonEntity;
import com.nthuy.demo_erm.entity.SystemEntity;
import com.nthuy.demo_erm.exception.BadRequestValidationException;
import com.nthuy.demo_erm.mapper.ReasonMapper;
import com.nthuy.demo_erm.repository.ReasonRepository;
import com.nthuy.demo_erm.repository.SystemRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReasonServiceImpl implements ReasonService {

    private final ReasonRepository reasonRepository;
    private final ReasonMapper reasonMapper;
    private final SystemRepository systemRepository;

    public ReasonServiceImpl(ReasonRepository reasonRepository, ReasonMapper reasonMapper, SystemRepository systemRepository) {
        this.reasonRepository = reasonRepository;
        this.reasonMapper = reasonMapper;
        this.systemRepository = systemRepository;
    }


    public boolean nameExists(String userName) {
        return this.reasonRepository.existsByName(userName);
    }


    public boolean existsById(Long id) {
        return this.reasonRepository.existsById(id);
    }

    public Long handleCreateClassifyReason(ReasonDTO dto) {
        ReasonEntity entity = reasonMapper.toEntity(dto);

        Set<SystemEntity> systemEntities;
        if (dto.getSystems() != null && !dto.getSystems().isEmpty()) {
            // Nếu DTO có truyền systems thì lấy theo danh sách đó
            systemEntities = dto.getSystems().stream()
                    .map(systemDTO -> systemRepository.findById(systemDTO.getId())
                            .orElseThrow(() -> new RuntimeException("System not found with id: " + systemDTO.getId())))
                    .collect(Collectors.toSet());
        } else {
            // Nếu không truyền thì mặc định lấy system có id = 1 và id = 2
            systemEntities = new HashSet<>(systemRepository.findAllById(Arrays.asList(1L, 2L)));
        }

        entity.setSystemEntitiesReason(systemEntities);

// In ra để kiểm tra
        System.out.println("Entity trước khi lưu: " + entity);

// Lưu xuống DB
        ReasonEntity savedEntity = reasonRepository.save(entity);

// Trả về ID
        return savedEntity.getId();
    }

    public ReasonDTO handleGetReasonById(Long id) {

        ReasonEntity reason = this.reasonRepository.findById(id)
                .orElseThrow(() -> new BadRequestValidationException("Thẻ bảo hiểm với ID " + id + " không tồn tại"));
        return reasonMapper.toDto(reason);

    }


    public void handleDeleteReason(Long id) {
        this.reasonRepository.deleteById(id);
    }


    public Long handleUpdateReason(ReasonDTO dto) {
        // Lấy entity cũ từ DB
        ReasonEntity reason = reasonRepository.findById(dto.getId())
                .orElseThrow(() -> new BadRequestValidationException(
                        "Phân loại nguyên nhân với ID " + dto.getId() + " không tồn tại"));
        reasonMapper.updateEntityFromDto(dto,reason);
        Set<SystemEntity> systemEntities;

        if (dto.getSystems() != null && !dto.getSystems().isEmpty()) {
            // Nếu DTO có truyền systems thì lấy theo danh sách đó
            systemEntities = dto.getSystems().stream()
                    .map(systemDTO -> systemRepository.findById(systemDTO.getId())
                            .orElseThrow(() -> new RuntimeException("System not found with id: " + systemDTO.getId())))
                    .collect(Collectors.toSet());
        } else {
            // Nếu không truyền thì mặc định lấy system có id = 1 và id = 2
            systemEntities = new HashSet<>(systemRepository.findAllById(Arrays.asList(1L, 2L)));
        }

        reason.setSystemEntitiesReason(systemEntities);
        return reasonRepository.save(reason).getId();
    }

}
