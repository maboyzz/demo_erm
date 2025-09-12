package com.nthuy.demo_erm.service;




import com.nthuy.demo_erm.common.constant.EnumTypeReason;
import com.nthuy.demo_erm.dto.ReasonDTO;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.common.exception.NameExisted;
import com.nthuy.demo_erm.service.dto.SearchRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ReasonService {


    Long create(ReasonDTO dto) throws NameExisted;

    ReasonDTO getReason(Long id);

    void delete(Long id);

    Long update(ReasonDTO dto) throws NameExisted;

    ResultPaginationDTO<ReasonDTO> getListReason(SearchRequest searchRequest, Pageable pageable);

}
