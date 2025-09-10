package com.nthuy.demo_erm.proxy;

import com.nthuy.demo_erm.dto.DepartmentDTO;
import com.nthuy.demo_erm.dto.EmployeeDTO;

import java.util.Map;
import java.util.Set;

public interface EmployeeProxy {
    Map<Long, EmployeeDTO> getEmployees(Set<Long> employeeIds);
    EmployeeDTO getEmployee(Long employeeId);
}
