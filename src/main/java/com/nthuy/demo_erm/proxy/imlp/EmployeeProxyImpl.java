package com.nthuy.demo_erm.proxy.imlp;

import com.nthuy.demo_erm.FeignClient.EmployeeFeignClient;
import com.nthuy.demo_erm.common.dto.ApiResponse;
import com.nthuy.demo_erm.dto.EmployeeDTO;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import com.nthuy.demo_erm.proxy.EmployeeProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Component
public class EmployeeProxyImpl implements EmployeeProxy {

    private final EmployeeFeignClient employeeFeignClient;

    Pageable pageable = PageRequest.of(0, 1000);
    @Value("${clients.resources.bearer-token}")
    String token;
    @Override
    public Map<Long, EmployeeDTO> getEmployees(Set<Long> employeeIds) {
        if (employeeIds == null || employeeIds.isEmpty()) {
            return Collections.emptyMap();
        }
        try {
            ApiResponse<ResultPaginationDTO<EmployeeDTO>> response =
                    employeeFeignClient.getEmployeeList(employeeIds, pageable,"apodio.dev.apusplatform.com","Bearer "+token);

            if (response != null
                    && response.getData() != null
                    && response.getData().getContent() != null) {
                return response.getData().getContent().stream()
                        .collect(Collectors.toMap(EmployeeDTO::getId, Function.identity()));
            }
        } catch (Exception e) {
            log.error("Error fetching employees from FeignClient", e);
        }
        return Collections.emptyMap();
    }
    @Override
    public EmployeeDTO getEmployee(Long employeeId) {
        if (employeeId == null) {
            return null;
        }

        try {
            Set<Long> employeeIds = Set.of(employeeId);

            ApiResponse<ResultPaginationDTO<EmployeeDTO>> response =
                    employeeFeignClient.getEmployeeList(
                            employeeIds,
                            Pageable.unpaged(),  // hoặc truyền pageable khác
                            "apodio.dev.apusplatform.com",
                            "Bearer " + token
                    );

            if (response != null
                    && response.getData() != null
                    && response.getData().getContent() != null) {
                return response.getData()
                        .getContent()
                        .stream()
                        .filter(emp -> emp != null && Objects.equals(emp.getId(), employeeId))
                        .findFirst()
                        .orElse(null);
            }
        } catch (Exception e) {
            log.error("Error fetching employee from FeignClient", e);
        }

        return null;
    }
}
