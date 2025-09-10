package com.nthuy.demo_erm.proxy;

import com.nthuy.demo_erm.dto.EmployeeDTO;
import com.nthuy.demo_erm.dto.SystemDTO;

import java.util.Map;
import java.util.Set;

public interface SystemProxy {
    Map<Long, SystemDTO> getSystems(Set<Long> systemIds);
    SystemDTO getSystem(Long systemId);

}