package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.dto.ReasonDTO;
import com.nthuy.demo_erm.dto.RiskCategoryDTO;
import com.nthuy.demo_erm.entity.ReasonEntity;
import com.nthuy.demo_erm.entity.RiskCategoryEntity;
import com.nthuy.demo_erm.entity.SystemEntity;
import com.nthuy.demo_erm.exception.BadRequestValidationException;
import com.nthuy.demo_erm.mapper.RiskCategoryMapper;
import com.nthuy.demo_erm.repository.RiskCategoryRepository;
import com.nthuy.demo_erm.repository.SystemRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RiskCategoryServiceImpl implements RiskCategoryService{

    private final RiskCategoryRepository riskCategoryRepository;
    private final RiskCategoryMapper riskCategoryMapper;
    private final SystemRepository systemRepository;

    public RiskCategoryServiceImpl(RiskCategoryRepository riskCategoryRepository, RiskCategoryMapper riskCategoryMapper, SystemRepository systemRepository) {
        this.riskCategoryRepository = riskCategoryRepository;
        this.riskCategoryMapper = riskCategoryMapper;
        this.systemRepository = systemRepository;
    }


    @Override
    public boolean nameExists(String userName) {
        return this.riskCategoryRepository.existsByName(userName);
    }

    @Override
    public boolean existsById(Long id) {
        return this.riskCategoryRepository.existsById(id);
    }

    @Override
    public Long handleCreateRiskCategory(RiskCategoryDTO dto) {
        RiskCategoryEntity entity = riskCategoryMapper.toEntity(dto);
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
        entity.setSystemEntitiesRiskCategory(systemEntities);
        // In ra để kiểm tra
        System.out.println("Entity trước khi lưu: " + entity);

        // Lưu xuống DB
        RiskCategoryEntity savedEntity = riskCategoryRepository.save(entity);

        // Trả về ID
        return savedEntity.getId();
    }

    public RiskCategoryDTO handleGetRiskCategoryById(Long id) {

        RiskCategoryEntity dto = this.riskCategoryRepository.findById(id)
                .orElseThrow(() -> new BadRequestValidationException(id + " không tồn tại"));
        return riskCategoryMapper.toDto(dto);

    }


    public void handleDeleteRiskCategory(Long id) {
        this.riskCategoryRepository.deleteById(id);
    }



    @Override
    public Long handleUpdateRiskCategory(RiskCategoryDTO dto) {
        RiskCategoryEntity entity = riskCategoryRepository.findById(dto.getId()) .orElseThrow(() -> new BadRequestValidationException(
                "Danh mục rủi ro với ID " + dto.getId() + " không tồn tại"));

        riskCategoryMapper.updateEntityFromDto(dto,entity);
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

        entity.setSystemEntitiesRiskCategory(systemEntities);
        return riskCategoryRepository.save(entity).getId();
    }



}
