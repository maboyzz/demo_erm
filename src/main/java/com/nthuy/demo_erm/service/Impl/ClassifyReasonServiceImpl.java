//package com.nthuy.demo_erm.service.Impl;
//
//import com.nthuy.demo_erm.FeignClient.SystemFeignClient;
//import com.nthuy.demo_erm.config.ClassifyReasonSpecification;
//import com.nthuy.demo_erm.dto.*;
//import com.nthuy.demo_erm.entity.ClassifyReasonEntity;
//import com.nthuy.demo_erm.entity.ClassifyReasonMapEntity;
//import com.nthuy.demo_erm.exception.BadRequestValidationException;
//import com.nthuy.demo_erm.exception.IdInvalidException;
//import com.nthuy.demo_erm.exception.NameExisted;
//import com.nthuy.demo_erm.mapper.ClassifyReasonMapper;
//import com.nthuy.demo_erm.repository.ClassifyReasonMapRepository;
//import com.nthuy.demo_erm.repository.ClassifyReasonRepository;
//import com.nthuy.demo_erm.service.ClassifyReasonService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.*;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class ClassifyReasonServiceImpl implements ClassifyReasonService {
//
//    private final ClassifyReasonRepository classifyReasonRepository;
//    private final ClassifyReasonMapper classifyReasonMapper;
//    private final SystemFeignClient systemFeignClient;
//    private final ClassifyReasonMapRepository classifyReasonMapRepository;
//
//    // ✅ Check name tồn tại (tạo thêm method trong repository)
//    private boolean nameExists(String name, Long excludeId) {
//        if (excludeId == null) {
//            return classifyReasonRepository.existsByName(name);
//        }
//        return classifyReasonRepository.existsByNameAndIdNot(name, excludeId);
//    }
//
//    private boolean existsById(Long id) {
//        return classifyReasonRepository.existsById(id);
//    }
//
//    // ---------------- CREATE ----------------
//    @Override
//    @Transactional
//    public Long create(ClassifyReasonDTO dto) throws NameExisted {
//        if (this.nameExists(dto.getName(), null)) {
//            throw new NameExisted("Name đã có");
//        }
//        ClassifyReasonEntity entity = classifyReasonMapper.toEntity(dto);
//      classifyReasonRepository.save(entity);
//
//        // xử lý systems
//        Set<Long> systemIds = (dto.getSystems() != null && !dto.getSystems().isEmpty())
//                ? dto.getSystems().stream().map(SystemDTO::getId).collect(Collectors.toSet())
//                : new HashSet<>(Arrays.asList(1L, 2L));
//
//        saveReasonSystemMap(entity.getId(), systemIds);
//
//        return entity.getId();
//    }
//
//    // ---------------- GET BY ID ----------------
//    @Override
//    public ClassifyReasonDTO getClassifyReason(Long id) {
//        ClassifyReasonEntity classifyReason = classifyReasonRepository.findById(id)
//                .orElseThrow(() -> new BadRequestValidationException("ID " + id + " không tồn tại"));
//
//        ClassifyReasonDTO dto = classifyReasonMapper.toDto(classifyReason);
//        dto.setSystems(getSystemsByReasonId(id));
//
//        return dto;
//    }
//    // ---------------- DELETE ----------------
//    @Override
//    public void delete(Long id) {
//        if (!this.existsById(id)) {
//            throw new IdInvalidException("id không có");
//        }
//        classifyReasonRepository.deleteById(id);
//    }
//
//    // ---------------- UPDATE ----------------
//    @Override
//    @Transactional
//    public Long update(ClassifyReasonDTO dto) throws NameExisted {
//        if (this.nameExists(dto.getName(), dto.getId())) {
//            throw new NameExisted("Name đã có");
//        }
//
//        ClassifyReasonEntity classifyReason = classifyReasonRepository.findById(dto.getId())
//                .orElseThrow(() -> new BadRequestValidationException(
//                        "Phân loại nguyên nhân với ID " + dto.getId() + " không tồn tại"));
//
//        classifyReasonMapper.updateEntityFromDto(dto, classifyReason);
//       classifyReasonRepository.save(classifyReason);
//
//        Set<Long> systemIds = (dto.getSystems() != null && !dto.getSystems().isEmpty())
//                ? dto.getSystems().stream().map(SystemDTO::getId).collect(Collectors.toSet())
//                : new HashSet<>(Arrays.asList(1L, 2L));
//
//        classifyReasonMapRepository.deleteByClassifyReasonId(classifyReason.getId());
//        saveReasonSystemMap(classifyReason.getId(), systemIds);
//
//        return classifyReason.getId();
//    }
//
//    // ---------------- GET LIST ----------------
//    @Override
//    @Transactional(readOnly = true)
//    public ResultPaginationDTO<ClassifyReasonDTO> getListClassifyReason(
//            String code, String name, Set<Long> systemIds, Pageable pageable) {
//
//        Specification<ClassifyReasonEntity> spec = Specification.where(null);
//
//        if (code != null && !code.isBlank()) {
//            spec = spec.and(ClassifyReasonSpecification.hasCode(code));
//        }
//        if (name != null && !name.isBlank()) {
//            spec = spec.and(ClassifyReasonSpecification.hasName(name));
//        }
////        if (systemIds != null && !systemIds.isEmpty()) {
////            spec = spec.and(ClassifyReasonSpecification.hasSystemIdIn(systemIds));
////        }
//
//        Page<ClassifyReasonEntity> pageResult = classifyReasonRepository.findAll(spec, pageable);
//        List<ClassifyReasonDTO> dtoList = classifyReasonMapper.toDtoList(pageResult.getContent());
//
//        // ⚡️ Chỉ gọi 1 lần FeignClient để tối ưu
//        Map<Long,SystemDTO> systemDTOMap = getMapSystems(systemIds);
//
//        for (ClassifyReasonDTO dto : dtoList) {
//            dto.setSystems(getSystemsByReasonId(dto.getId(),systemDTOMap ));
//        }
//
//        Meta meta = new Meta();
//        meta.setPage(pageResult.getNumber());
//        meta.setSize(pageResult.getSize());
//        meta.setTotalElements(pageResult.getTotalElements());
//        meta.setTotalPages(pageResult.getTotalPages());
//        meta.setNumberOfElements(pageResult.getNumberOfElements());
//        meta.setSort(pageable.getSort().toString());
//
//        ResultPaginationDTO<ClassifyReasonDTO> result = new ResultPaginationDTO<>();
//        result.setContent(dtoList);
//        result.setMeta(meta);
//
//        return result;
//    }
//
//    // ---------------- HELPER ----------------
//    private void saveReasonSystemMap(Long reasonId, Set<Long> systemIds) {
//        for (Long systemId : systemIds) {
//            ClassifyReasonMapEntity mapEntity = new ClassifyReasonMapEntity();
//            mapEntity.setClassifyReasonId(reasonId);
//            mapEntity.setSystemId(systemId);
//            classifyReasonMapRepository.save(mapEntity);
//        }
//    }
//
//    private Set<SystemDTO> getSystemsByReasonId(Long reasonId) {
//        return getSystemsByReasonId(reasonId, getMapSystems());
//    }
//
//    private Set<SystemDTO> getSystemsByReasonId(Long reasonId, List<SystemDTO> allSystems) {
//        List<ClassifyReasonMapEntity> mapEntities = classifyReasonMapRepository.findByClassifyReasonId(reasonId);
//
//        if (mapEntities.isEmpty()) {
//            return Collections.emptySet();
//        }
//
//        Set<Long> systemIds = mapEntities.stream()
//                .map(ClassifyReasonMapEntity::getSystemId)
//                .collect(Collectors.toSet());
//
//        return allSystems.stream()
//                .filter(system -> systemIds.contains(system.getId()))
//                .collect(Collectors.toSet());
//    }
//
//    private Map<Long, SystemDTO> getMapSystems(Set<Long> systemIds) {
//        try {
//            ApiResponse<ResultPaginationDTO<SystemDTO>> response =
//                    systemFeignClient.getSystemList(systemIds, 0, 1000);
//
//            return response.getData()
//                    .getContent()
//                    .stream()
//                    .collect(Collectors.toMap(SystemDTO::getId, Function.identity()));
//
//        } catch (Exception e) {
//            return new HashMap<>();
//        }
//    }
//}