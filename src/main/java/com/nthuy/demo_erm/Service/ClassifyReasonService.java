package com.nthuy.demo_erm.Service;

import com.nthuy.demo_erm.DTO.ClassifyReasonDTO;
import com.nthuy.demo_erm.DTO.SystemDTO;
import com.nthuy.demo_erm.Entity.ClassifyReasonEntity;
import com.nthuy.demo_erm.Entity.ClassifyReasonEntity;
import com.nthuy.demo_erm.Entity.SystemEntity;
import com.nthuy.demo_erm.Exception.BadRequestValidationException;
import com.nthuy.demo_erm.Repository.ClassifyReasonRepository;
import com.nthuy.demo_erm.Repository.SystemRepository;
import org.springframework.stereotype.Service;

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

    public ClassifyReasonDTO handleGetById(Long id){

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


}
