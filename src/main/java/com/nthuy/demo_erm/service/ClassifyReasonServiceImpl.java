package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.FeignClient.SystemFeignClient;
import com.nthuy.demo_erm.config.ClassifyReasonSpecification;
import com.nthuy.demo_erm.dto.*;
import com.nthuy.demo_erm.entity.ClassifyReasonEntity;
import com.nthuy.demo_erm.entity.ClassifyReasonMapEntity;
import com.nthuy.demo_erm.exception.BadRequestValidationException;
import com.nthuy.demo_erm.exception.IdInvalidException;
import com.nthuy.demo_erm.exception.NameExisted;
import com.nthuy.demo_erm.mapper.ClassifyReasonMapper;
import com.nthuy.demo_erm.repository.ClassifyReasonMapRepository;
import com.nthuy.demo_erm.repository.ClassifyReasonRepository;
import com.nthuy.demo_erm.until.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassifyReasonServiceImpl implements ClassifyReasonService {

    private final ClassifyReasonRepository classifyReasonRepository;
    private final ClassifyReasonMapper classifyReasonMapper;
    private final SystemFeignClient systemFeignClient;
    private final ClassifyReasonMapRepository classifyReasonMapRepository;

    public void validateNameNotExists(String name, Long excludeId) throws NameExisted {
        boolean exists;
        if (excludeId == null) {
            exists = classifyReasonRepository.existsByName(name);
        } else {
            exists = classifyReasonRepository.existsByNameAndIdNot(name, excludeId);
        }

        if (exists) {
            throw new NameExisted("Name đã tồn tại: " + name);
        }
    }

    // Check tồn tại ID
    public void validateIdExists(Long id) {
        if (!classifyReasonRepository.existsById(id)) {
            throw new IdInvalidException("Id không tồn tại: " + id);
        }
    }


    // ---------------- CREATE ----------------
    @Override
    @Transactional
    public Long create(ClassifyReasonDTO dto) throws NameExisted {
        this.validateNameNotExists(dto.getName(), null);
        ClassifyReasonEntity entity = classifyReasonMapper.toEntity(dto);
        classifyReasonRepository.save(entity);

        // xử lý systems
        Set<Long> systemIds = (dto.getSystems() != null && !dto.getSystems().isEmpty()) ? dto.getSystems().stream().map(SystemDTO::getId).collect(Collectors.toSet()) : new HashSet<>(Arrays.asList(1L, 2L));

        saveReasonSystemMap(entity.getId(), systemIds);

        return entity.getId();
    }

    // ---------------- GET BY ID ----------------
    @Override
    public ClassifyReasonDTO getClassifyReason(Long id) {
        ClassifyReasonEntity classifyReason = classifyReasonRepository.findById(id).orElseThrow(() -> new BadRequestValidationException("ID " + id + " không tồn tại"));

        ClassifyReasonDTO dto = classifyReasonMapper.toDto(classifyReason);

        // Tối ưu: Chỉ lấy systems cho 1 reason này
        dto.setSystems(getSystemsByReasonId(id));

        return dto;
    }

    // ---------------- DELETE ----------------
    @Override
    public void delete(Long id) {
        this.validateIdExists(id);
        classifyReasonRepository.deleteById(id);
    }

    // ---------------- UPDATE ----------------
    @Override
    @Transactional
    public Long update(ClassifyReasonDTO dto) throws NameExisted {
        this.validateNameNotExists(dto.getName(), dto.getId());

        ClassifyReasonEntity classifyReason = classifyReasonRepository.findById(dto.getId()).orElseThrow(() -> new BadRequestValidationException("Phân loại nguyên nhân với ID " + dto.getId() + " không tồn tại"));

        classifyReasonMapper.updateEntityFromDto(dto, classifyReason);
        classifyReasonRepository.save(classifyReason);

        Set<Long> systemIds = (dto.getSystems() != null && !dto.getSystems().isEmpty()) ? dto.getSystems().stream().map(SystemDTO::getId).collect(Collectors.toSet()) : new HashSet<>(Arrays.asList(1L, 2L));

        classifyReasonMapRepository.deleteByClassifyReasonId(classifyReason.getId());
        saveReasonSystemMap(classifyReason.getId(), systemIds);

        return classifyReason.getId();
    }

    // ---------------- GET LIST - ĐÃ TỐI ƯU ----------------
    @Override
    @Transactional(readOnly = true)
    public ResultPaginationDTO<ClassifyReasonDTO> getListClassifyReason(String code, String name, List<Long> systemIds, Pageable pageable) {

        Specification<ClassifyReasonEntity> spec = Specification.where(null);

        if (code != null && !code.isBlank()) {
            spec = spec.and(ClassifyReasonSpecification.hasCode(code));
        }
        if (name != null && !name.isBlank()) {
            spec = spec.and(ClassifyReasonSpecification.hasName(name));
        }
        if (systemIds != null && !systemIds.isEmpty()) {
            spec = spec.and(ClassifyReasonSpecification.hasSystemIdIn(systemIds));
        }

        Page<ClassifyReasonEntity> pageResult = classifyReasonRepository.findAll(spec, pageable);

        List<ClassifyReasonDTO> dtoList = classifyReasonMapper.toDtoList(pageResult.getContent());

        //Thu thập tất cả systemIds cần thiết từ tất cả reasons
        Set<Long> allSystemIds = new HashSet<>();
        List<Long> reasonIds = dtoList.stream().map(ClassifyReasonDTO::getId).collect(Collectors.toList());

        //Lấy tất cả mapping của các reasons này
        List<ClassifyReasonMapEntity> allMappings = classifyReasonMapRepository.findByClassifyReasonIdIn(reasonIds);
        allMappings.forEach(mapping -> allSystemIds.add(mapping.getSystemId()));

        //CHỈ GỌI 1 LẦN FeignClient để lấy tất cả systems cần thiết
        Map<Long, SystemDTO> systemDTOMap = getMapSystems(allSystemIds);

        // Gán systems cho từng reason
        for (ClassifyReasonDTO dto : dtoList) {
            dto.setSystems(getSystemsByClassifyReasonIdOptimized(dto.getId(), allMappings, systemDTOMap));
        }

        return PaginationUtils.buildResult(pageResult, dtoList, pageable);
    }

    // ---------------- HELPER METHODS - ĐÃ TỐI ƯU ----------------
    private void saveReasonSystemMap(Long reasonId, Set<Long> systemIds) {
        List<ClassifyReasonMapEntity> mapEntities = systemIds.stream().map(systemId -> {
            ClassifyReasonMapEntity mapEntity = new ClassifyReasonMapEntity();
            mapEntity.setClassifyReasonId(reasonId);
            mapEntity.setSystemId(systemId);
            return mapEntity;
        }).collect(Collectors.toList());

        classifyReasonMapRepository.saveAll(mapEntities);
    }

    // Method cho GET single record
    private Set<SystemDTO> getSystemsByReasonId(Long reasonId) {
        List<ClassifyReasonMapEntity> mapEntities = classifyReasonMapRepository.findByClassifyReasonId(reasonId);

        if (mapEntities.isEmpty()) {
            return Collections.emptySet();
        }

        Set<Long> systemIds = mapEntities.stream().map(ClassifyReasonMapEntity::getSystemId).collect(Collectors.toSet());

        Map<Long, SystemDTO> systemDTOMap = getMapSystems(systemIds);

        return systemIds.stream().map(systemDTOMap::get).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    // Method tối ưu cho GET list - sử dụng data đã có
    private Set<SystemDTO> getSystemsByClassifyReasonIdOptimized(Long classifyReasonId, List<ClassifyReasonMapEntity> allMappings, Map<Long, SystemDTO> systemDTOMap) {
        Set<Long> systemIds = allMappings.stream().filter(mapping -> mapping.getClassifyReasonId().equals(classifyReasonId)).map(ClassifyReasonMapEntity::getSystemId).collect(Collectors.toSet());

        return systemIds.stream().map(systemDTOMap::get).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    // Method gọi FeignClient
    private Map<Long, SystemDTO> getMapSystems(Set<Long> systemIds) {
        if (systemIds.isEmpty()) {
            return new HashMap<>();
        }
        try {
            ApiResponse<ResultPaginationDTO<SystemDTO>> response = systemFeignClient.getSystemList(systemIds, 0, 1000);

            if (response != null && response.getData() != null && response.getData().getContent() != null) {
                return response.getData().getContent().stream().collect(Collectors.toMap(SystemDTO::getId, Function.identity()));
            }
            return new HashMap<>();
        } catch (Exception e) {
            return new HashMap<>();
        }

    }
}