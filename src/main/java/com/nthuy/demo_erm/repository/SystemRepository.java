package com.nthuy.demo_erm.repository;


import com.nthuy.demo_erm.entity.SystemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemRepository extends JpaRepository<SystemEntity, Long> {
}
