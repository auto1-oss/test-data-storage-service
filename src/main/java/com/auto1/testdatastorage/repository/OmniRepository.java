package com.auto1.testdatastorage.repository;

import com.auto1.testdatastorage.domain.OmniQueueItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OmniRepository extends
        JpaRepository<OmniQueueItem, Long>, JpaSpecificationExecutor<OmniQueueItem> {

    List<OmniQueueItem> findAllByDataType(String dataType);

}
