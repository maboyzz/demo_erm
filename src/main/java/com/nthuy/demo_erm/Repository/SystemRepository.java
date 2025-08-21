package com.nthuy.demo_erm.Repository;


import com.nthuy.demo_erm.Entity.SystemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemRepository extends JpaRepository<SystemEntity, Long> {
}
