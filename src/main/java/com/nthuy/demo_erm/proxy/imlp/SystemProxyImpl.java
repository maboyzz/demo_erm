package com.nthuy.demo_erm.proxy.imlp;

import com.nthuy.demo_erm.FeignClient.SystemFeignClient;
import com.nthuy.demo_erm.common.dto.ApiResponse;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.dto.SystemDTO;
import com.nthuy.demo_erm.proxy.SystemProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Component
public class SystemProxyImpl implements SystemProxy {

    private final SystemFeignClient systemFeignClient;

    @Override
    public Map<Long, SystemDTO> getSystems(Set<Long> systemIds) {
        if (systemIds == null || systemIds.isEmpty()) {
            return Collections.emptyMap();
        }
        try {
            ApiResponse<ResultPaginationDTO<SystemDTO>> response =
                    systemFeignClient.getSystemList(systemIds, 0, 1000);

            if (response != null
                    && response.getData() != null
                    && response.getData().getContent() != null) {
                return response.getData().getContent().stream()
                        .collect(Collectors.toMap(SystemDTO::getId, Function.identity()));
            }
        } catch (Exception e) {
            log.error("Error fetching systems from FeignClient", e);
        }
        return Collections.emptyMap();
    }
}