package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.config.ClassifyReasonSpecification;
import com.nthuy.demo_erm.dto.ClassifyReasonDTO;
import com.nthuy.demo_erm.dto.SystemDTO;
import com.nthuy.demo_erm.entity.ClassifyReasonEntity;
import com.nthuy.demo_erm.entity.SystemEntity;
import com.nthuy.demo_erm.exception.BadRequestValidationException;
import com.nthuy.demo_erm.mapper.ClassifyReasonMapper;
import com.nthuy.demo_erm.repository.ClassifyReasonRepository;
import com.nthuy.demo_erm.repository.SystemRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClassifyReasonService {

    private final ClassifyReasonRepository classifyReasonRepository;
    private final SystemRepository systemRepository;
    private final ClassifyReasonMapper classifyReasonMapper;

    public ClassifyReasonService(ClassifyReasonRepository classifyReasonRepository, SystemRepository systemRepository, ClassifyReasonMapper classifyReasonMapper) {
        this.classifyReasonRepository = classifyReasonRepository;
        this.systemRepository = systemRepository;
        this.classifyReasonMapper = classifyReasonMapper;
    }


    public boolean nameExists(String userName) {
        return this.classifyReasonRepository.existsByName(userName);
    }
    public boolean existsById(Long id) {
        return classifyReasonRepository.existsById(id);
    }



    public Long handleCreateClassifyReason(ClassifyReasonDTO dto) {
        // Dùng mapper để chuyển DTO sang Entity
        ClassifyReasonEntity entity = classifyReasonMapper.toEntity(dto);

        // Xử lý ánh xạ hệ thống (systems) từ DTO sang Entity
        if (dto.getSystems() != null && !dto.getSystems().isEmpty()) {
            Set<SystemEntity> systemEntities = dto.getSystems().stream()
                    .map(systemDTO -> systemRepository.findById(systemDTO.getId())
                            .orElseThrow(() -> new RuntimeException("System not found with id: " + systemDTO.getId())))
                    .collect(Collectors.toSet());

            entity.setSystemEntities(systemEntities);
        }

        // In ra để kiểm tra
        System.out.println("Entity trước khi lưu: " + entity);

        // Lưu xuống DB
        ClassifyReasonEntity savedEntity = classifyReasonRepository.save(entity);

        // Trả về ID
        return savedEntity.getId();
    }


    public ClassifyReasonDTO handleGetClassifyReasonById(Long id){

        ClassifyReasonEntity classifyReason = this.classifyReasonRepository.findById(id)
                .orElseThrow(() -> new BadRequestValidationException("Thẻ bảo hiểm với ID " + id + " không tồn tại"));
        return new ClassifyReasonDTO(
                classifyReason.getId(),
                classifyReason.getCode(),
                classifyReason.getName(),
                classifyReason.getDescription(),
                classifyReason.getNote(),
                classifyReason.getSystemEntities()
                        .stream()
                        .map(se -> new SystemDTO(se.getId(), se.getName()))
                        .collect(Collectors.toSet())
        );

    }
    public List<ClassifyReasonDTO> handleGetClassifyReason() {
        List<ClassifyReasonEntity> entities = this.classifyReasonRepository.findAll();

        return entities.stream()
                .map(entity -> new ClassifyReasonDTO(
                        entity.getId(),
                        entity.getCode(),
                        entity.getName(),
                        entity.getDescription(),
                        entity.getNote(),
                        entity.getSystemEntities()
                                .stream()
                                .map(se -> new SystemDTO(se.getId(), se.getName()))
                                .collect(Collectors.toSet())
                ))
                .collect(Collectors.toList());
    }
    public void handleDeleteClassifyReason(Long id){
        this.classifyReasonRepository.deleteById(id);
    }


    public List<ClassifyReasonDTO> handleGetClassifyReason(String code, String name, List<Long> systemIds) {
        Specification<ClassifyReasonEntity> spec = Specification
                .where(ClassifyReasonSpecification.hasCode(code))
                .and(ClassifyReasonSpecification.hasName(name))
                .and(ClassifyReasonSpecification.hasSystemIdIn(systemIds));

        List<ClassifyReasonEntity> entities = classifyReasonRepository.findAll(spec);

        return entities.stream()
                .map(entity -> new ClassifyReasonDTO(
                        entity.getId(),
                        entity.getCode(),
                        entity.getName(),
                        entity.getDescription(),
                        entity.getNote(),
                        entity.getSystemEntities()
                                .stream()
                                .map(se -> new SystemDTO(se.getId(), se.getName()))
                                .collect(Collectors.toSet())
                ))
                .collect(Collectors.toList());
    }
}
