package com.nthuy.demo_erm.service.Impl;

import com.nthuy.demo_erm.config.RiskCategorySpecification;
import com.nthuy.demo_erm.dto.Meta;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.RiskCategoryDTO;
import com.nthuy.demo_erm.entity.RiskCategoryEntity;
import com.nthuy.demo_erm.entity.SystemEntity;
import com.nthuy.demo_erm.exception.BadRequestValidationException;
import com.nthuy.demo_erm.exception.IdInvalidException;
import com.nthuy.demo_erm.exception.NameExisted;
import com.nthuy.demo_erm.mapper.RiskCategoryMapper;
import com.nthuy.demo_erm.repository.RiskCategoryRepository;
import com.nthuy.demo_erm.repository.SystemRepository;
import com.nthuy.demo_erm.service.RiskCategoryService;
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
public class RiskCategoryServiceImpl implements RiskCategoryService {

    private final RiskCategoryRepository riskCategoryRepository;
    private final RiskCategoryMapper riskCategoryMapper;
    private final SystemRepository systemRepository;


    private boolean nameExists(String userName) {
        return this.riskCategoryRepository.existsByName(userName);
    }

    private boolean existsById(Long id) {
        return this.riskCategoryRepository.existsById(id);
    }

    @Override
    public Long handleCreateRiskCategory(RiskCategoryDTO dto) throws NameExisted {

        if (this.nameExists(dto.getName())) {
            throw new NameExisted("Name đã có");
        }
        if (this.existsById(dto.getId())) {
            throw new IdInvalidException("Id cha không có" + dto.getId());
        }

        RiskCategoryEntity entity = riskCategoryMapper.toEntity(dto);
        Set<SystemEntity> systemEntities;
        if (dto.getSystems() != null && !dto.getSystems().isEmpty()) {
            // Nếu DTO có truyền systems thì lấy theo danh sách đó
            systemEntities = dto.getSystems().stream().map(systemDTO -> systemRepository.findById(systemDTO.getId()).orElseThrow(() -> new RuntimeException("System not found with id: " + systemDTO.getId()))).collect(Collectors.toSet());
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

    @Override
    public RiskCategoryDTO handleGetRiskCategoryById(Long id) {

        RiskCategoryEntity entity = this.riskCategoryRepository.findById(id).orElseThrow(() -> new BadRequestValidationException(id + " không tồn tại"));
        return riskCategoryMapper.toDto(entity);

    }

    @Override
    public void handleDeleteRiskCategory(Long id) {
        if (this.existsById(id)) {
            throw new IdInvalidException("Id không có" + id);
        }
        this.riskCategoryRepository.deleteById(id);
    }

    @Override
    public Long handleUpdateRiskCategory(RiskCategoryDTO dto) throws NameExisted {
        if (this.nameExists(dto.getName())) {
            throw new NameExisted("Name đã có");
        }
        Long parentId = dto.getParent_id();
        if (parentId != null) {
            // 1) parent không được trùng với chính nó
            if (parentId.equals(dto.getId())) {
                throw new IdInvalidException("ID danh mục cha không được trùng với chính nó: " + parentId);
            }
            // 2) parent phải tồn tại
            boolean isValidParent = this.existsById(parentId);
            if (!isValidParent) {
                throw new IdInvalidException("ID danh mục cha " + parentId + " không có");
            }
        }
        RiskCategoryEntity entity = riskCategoryRepository.findById(dto.getId()).orElseThrow(() -> new BadRequestValidationException("Danh mục rủi ro với ID " + dto.getId() + " không tồn tại"));

        riskCategoryMapper.updateEntityFromDto(dto, entity);
        Set<SystemEntity> systemEntities;

        if (dto.getSystems() != null && !dto.getSystems().isEmpty()) {
            // Nếu DTO có truyền systems thì lấy theo danh sách đó
            systemEntities = dto.getSystems().stream().map(systemDTO -> systemRepository.findById(systemDTO.getId()).orElseThrow(() -> new RuntimeException("System not found with id: " + systemDTO.getId()))).collect(Collectors.toSet());
        } else {
            // Nếu không truyền thì mặc định lấy system có id = 1 và id = 2
            systemEntities = new HashSet<>(systemRepository.findAllById(Arrays.asList(1L, 2L)));
        }

        entity.setSystemEntitiesRiskCategory(systemEntities);
        return riskCategoryRepository.save(entity).getId();
    }

    @Override
    public ResultPaginationDTO<RiskCategoryDTO> handleGetRiskCategory(String code, String name, List<Long> systemIds, Boolean isActive, Pageable pageable) {

        Specification<RiskCategoryEntity> spec = Specification.where(RiskCategorySpecification.hasCode(code)).
                and(RiskCategorySpecification.hasName(name)).
                and(RiskCategorySpecification.hasSystemIdIn(systemIds)).
                and(RiskCategorySpecification.hasIsActive(isActive));

        Page<RiskCategoryEntity> pageResult = riskCategoryRepository.findAll(spec, pageable);

        List<RiskCategoryDTO> dtoList = riskCategoryMapper.toDtoList(pageResult.getContent());

        Meta meta = new Meta();
        meta.setPage(pageResult.getNumber());
        meta.setSize(pageResult.getSize());
        meta.setTotalElements(pageResult.getTotalElements());
        meta.setTotalPages(pageResult.getTotalPages());
        meta.setNumberOfElements(pageResult.getNumberOfElements());
        meta.setSort(pageable.getSort().toString());

        ResultPaginationDTO<RiskCategoryDTO> result = new ResultPaginationDTO<>();
        result.setContent(dtoList);
        result.setMeta(meta);

        return result;
    }

}
