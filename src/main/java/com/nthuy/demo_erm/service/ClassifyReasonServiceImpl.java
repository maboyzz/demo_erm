package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.config.ClassifyReasonSpecification;
import com.nthuy.demo_erm.dto.ClassifyReasonDTO;
import com.nthuy.demo_erm.dto.Meta;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.SystemDTO;
import com.nthuy.demo_erm.entity.ClassifyReasonEntity;
import com.nthuy.demo_erm.entity.SystemEntity;
import com.nthuy.demo_erm.exception.BadRequestValidationException;
import com.nthuy.demo_erm.mapper.ClassifyReasonMapper;
import com.nthuy.demo_erm.repository.ClassifyReasonRepository;
import com.nthuy.demo_erm.repository.SystemRepository;
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
public class ClassifyReasonServiceImpl implements ClassifyReasonService {

    private final ClassifyReasonRepository classifyReasonRepository;
    private final SystemRepository systemRepository;
    private final ClassifyReasonMapper classifyReasonMapper;

    public ClassifyReasonServiceImpl(ClassifyReasonRepository classifyReasonRepository, SystemRepository systemRepository, ClassifyReasonMapper classifyReasonMapper) {
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

    entity.setSystemEntities(systemEntities);

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
        return classifyReasonMapper.toDto(classifyReason);

    }

    public void handleDeleteClassifyReason(Long id){
        this.classifyReasonRepository.deleteById(id);
    }

//    public Long handleUpdateClassifyReason(ClassifyReasonDTO dto) {
//        ClassifyReasonEntity classifyReason = this.classifyReasonRepository.findById(dto.getId())
//                .orElseThrow(() -> new BadRequestValidationException("Thẻ bảo hiểm với ID " + dto.getId() + " không tồn tại"));
//        classifyReason = classifyReasonMapper.toEntity(dto);
//
//        if (dto.getSystems() != null && !dto.getSystems().isEmpty()) {
//            Set<SystemEntity> systemEntities = dto.getSystems().stream()
//                    .map(systemDTO -> systemRepository.findById(systemDTO.getId())
//                            .orElseThrow(() -> new RuntimeException("System not found with id: " + systemDTO.getId())))
//                    .collect(Collectors.toSet());
//
//            classifyReason.setSystemEntities(systemEntities);
//        }
//         ClassifyReasonEntity savedEntity = classifyReasonRepository.save(classifyReason);
//         return savedEntity.getId();
//
//    }
public Long handleUpdateClassifyReason(ClassifyReasonDTO dto) {
    // Lấy entity cũ từ DB
    ClassifyReasonEntity classifyReason = classifyReasonRepository.findById(dto.getId())
            .orElseThrow(() -> new BadRequestValidationException(
                    "Phân loại nguyên nhân với ID " + dto.getId() + " không tồn tại"));

    // MapStruct copy field từ DTO sang entity (trừ systems, ta xử lý riêng)
    classifyReasonMapper.updateEntityFromDto(dto, classifyReason);

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

    classifyReason.setSystemEntities(systemEntities);

    return classifyReasonRepository.save(classifyReason).getId();
}


    public ResultPaginationDTO<ClassifyReasonDTO> handleGetClassifyReason(
            String code, String name, List<Long> systemIds, Pageable pageable) {

        Specification<ClassifyReasonEntity> spec = Specification
                .where(ClassifyReasonSpecification.hasCode(code))
                .and(ClassifyReasonSpecification.hasName(name))
                .and(ClassifyReasonSpecification.hasSystemIdIn(systemIds));

        Page<ClassifyReasonEntity> pageResult = classifyReasonRepository.findAll(spec, pageable);

        List<ClassifyReasonDTO> dtoList = classifyReasonMapper.toDtoList(pageResult.getContent());

        Meta meta = new Meta();
        meta.setPage(pageResult.getNumber());
        meta.setSize(pageResult.getSize());
        meta.setTotalElements(pageResult.getTotalElements());
        meta.setTotalPages(pageResult.getTotalPages());
        meta.setNumberOfElements(pageResult.getNumberOfElements());
        meta.setSort(pageable.getSort().toString());

        ResultPaginationDTO<ClassifyReasonDTO> result = new ResultPaginationDTO<>();
        result.setContent(dtoList);
        result.setMeta(meta);

        return result;
    }
}
