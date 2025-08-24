package com.nthuy.demo_erm.service;




import com.nthuy.demo_erm.dto.ReasonDTO;



public interface ReasonService {


    boolean nameExists(String userName);

    boolean existsById(Long id);

    Long handleCreateClassifyReason(ReasonDTO dto);

    ReasonDTO handleGetReasonById(Long id);

    void handleDeleteReason(Long id);

    Long handleUpdateReason(ReasonDTO dto);
}
