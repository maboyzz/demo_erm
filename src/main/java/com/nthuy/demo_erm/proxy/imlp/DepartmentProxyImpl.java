package com.nthuy.demo_erm.proxy.imlp;

import com.nthuy.demo_erm.FeignClient.DepartmentFeignClient;
import com.nthuy.demo_erm.common.dto.ApiResponse;
import com.nthuy.demo_erm.dto.DepartmentDTO;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.proxy.DepartmentProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Component
public class DepartmentProxyImpl implements DepartmentProxy {

    private final DepartmentFeignClient departmentFeignClient;
    Pageable pageable = PageRequest.of(0, 1000);
    @Value("${clients.uaa.bearer-token}")
    String token;

    @Override
    public Map<Long, DepartmentDTO> getDepartments(Set<Long> departmentIds) {
        if (departmentIds == null || departmentIds.isEmpty()) {
            return Collections.emptyMap();
        }
        try {
            ApiResponse<ResultPaginationDTO<DepartmentDTO>> response =
                    departmentFeignClient.getDepartmentList(departmentIds, pageable,"201","Bearer "+token);

            if (response != null
                    && response.getData() != null
                    && response.getData().getContent() != null) {
                return response.getData().getContent().stream()
                        .collect(Collectors.toMap(DepartmentDTO::getId, Function.identity()));
            }
        } catch (Exception e) {
            log.error("Error fetching systems from FeignClient", e);
        }
        return Collections.emptyMap();
    }
}
