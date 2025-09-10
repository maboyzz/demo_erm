package com.nthuy.demo_erm.repository;

import com.nthuy.demo_erm.entity.ReasonEntity;
import com.nthuy.demo_erm.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long>, JpaSpecificationExecutor<TagEntity> {

}
