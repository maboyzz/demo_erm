package com.nthuy.demo_erm.FeignClient;

import com.nthuy.demo_erm.common.dto.ApiResponse;
import com.nthuy.demo_erm.dto.EmployeeDTO;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(name = "reactive", url = "${clients.reactive.url}")
public interface EmployeeFeignClient {
    @GetMapping("/api/v1/employee/list")
    ApiResponse<ResultPaginationDTO<EmployeeDTO>> getEmployeeList(
            @RequestParam(required = false) Set<Long> ids,
            Pageable pageable,
            @RequestHeader("current-domain") String tenantId,
            @RequestHeader("Authorization") String bearerToken
    );
}
