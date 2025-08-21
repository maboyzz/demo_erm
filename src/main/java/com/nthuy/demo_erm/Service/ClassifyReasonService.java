package com.nthuy.demo_erm.Service;

import com.nthuy.demo_erm.Config.ClassifyReasonSpecification;
import com.nthuy.demo_erm.DTO.ClassifyReasonDTO;
import com.nthuy.demo_erm.DTO.IdResponse;
import com.nthuy.demo_erm.DTO.SystemDTO;
import com.nthuy.demo_erm.Entity.ClassifyReasonEntity;
import com.nthuy.demo_erm.Entity.ClassifyReasonEntity;
import com.nthuy.demo_erm.Entity.SystemEntity;
import com.nthuy.demo_erm.Exception.BadRequestValidationException;
import com.nthuy.demo_erm.Repository.ClassifyReasonRepository;
import com.nthuy.demo_erm.Repository.SystemRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClassifyReasonService {

    private final ClassifyReasonRepository classifyReasonRepository;
    private final SystemRepository systemRepository;

    public ClassifyReasonService(ClassifyReasonRepository classifyReasonRepository, SystemRepository systemRepository) {
        this.classifyReasonRepository = classifyReasonRepository;
        this.systemRepository = systemRepository;

    }


    public boolean nameExists(String userName) {
        return this.classifyReasonRepository.existsByName(userName);
    }
    public boolean existsById(Long id) {
        return classifyReasonRepository.existsById(id);
    }




    public Long handleCreateClassifyReason(ClassifyReasonDTO dto) {
        // Tạo entity và gán giá trị thủ công từ DTO
        ClassifyReasonEntity entity = new ClassifyReasonEntity();
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setNote(dto.getNote());

        // Nếu DTO có chứa danh sách hệ thống (systems)
        if (dto.getSystems() != null && !dto.getSystems().isEmpty()) {
            Set<SystemEntity> systemEntities = dto.getSystems().stream()
                    .map(s -> systemRepository.findById(s.getId())
                            .orElseThrow(() -> new RuntimeException("System not found with id: " + s.getId())))
                    .collect(Collectors.toSet());

            entity.setSystemEntities(systemEntities); // set vào entity
        }

        // In ra để kiểm tra
        java.lang.System.out.println("Entity trước khi lưu: " + entity);

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
