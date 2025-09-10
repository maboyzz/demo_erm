package com.nthuy.demo_erm.FeignClient;

import com.nthuy.demo_erm.config.FeignConfig;
import com.nthuy.demo_erm.common.dto.ApiResponse;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.SystemDTO;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(name = "uaa-client", url = "${clients.uaa.uaa-url}")
public interface SystemFeignClient {
    @GetMapping("/api/v1/system/list")
    ApiResponse<ResultPaginationDTO<SystemDTO>> getSystemList(
            @RequestParam(required = false) Set<Long> ids,
            Pageable pageable
    );
}
