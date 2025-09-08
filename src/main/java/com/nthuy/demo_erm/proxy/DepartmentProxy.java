package com.nthuy.demo_erm.proxy;

import com.nthuy.demo_erm.dto.DepartmentDTO;


import java.util.Map;
import java.util.Set;

public interface DepartmentProxy {
    Map<Long, DepartmentDTO> getDepartments(Set<Long> departmentIds);

}
