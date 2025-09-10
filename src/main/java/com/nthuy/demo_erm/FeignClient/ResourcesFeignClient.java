package com.nthuy.demo_erm.FeignClient;

import com.nthuy.demo_erm.common.dto.ApiResponse;
import com.nthuy.demo_erm.dto.DepartmentDTO;
import com.nthuy.demo_erm.dto.EmployeeDTO;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;
@FeignClient(name = "resources", url = "${clients.resources.resources-url}")
public interface ResourcesFeignClient {
    @GetMapping("/api/v1/department/list")
    ApiResponse<ResultPaginationDTO<DepartmentDTO>> getDepartmentList(
            @RequestParam(required = false) Set<Long> ids,
            Pageable pageable,
            @RequestHeader("X-TenantId") String tenantId,
            @RequestHeader("Authorization") String bearerToken
    );

}


