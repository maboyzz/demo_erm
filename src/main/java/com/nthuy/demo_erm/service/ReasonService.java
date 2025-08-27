package com.nthuy.demo_erm.service;




import com.nthuy.demo_erm.constant.EnumTypeReason;
import com.nthuy.demo_erm.dto.ClassifyReasonDTO;
import com.nthuy.demo_erm.dto.ReasonDTO;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.exception.NameExisted;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ReasonService {


    Long handleCreateClassifyReason(ReasonDTO dto) throws NameExisted;

    ReasonDTO handleGetReasonById(Long id);

    void handleDeleteReason(Long id);

    Long handleUpdateReason(ReasonDTO dto) throws NameExisted;

    ResultPaginationDTO<ReasonDTO> handleGetReason(String code, String name, List<Long> systemIds, Boolean isActive, EnumTypeReason type, Pageable pageable);

}
