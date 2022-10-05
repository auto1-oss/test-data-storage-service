package com.auto1.testdatastorage.repository;

import com.auto1.testdatastorage.domain.OmniType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OmniTypeRepository
        extends JpaRepository<OmniType, Long>, JpaSpecificationExecutor<OmniType> {

    Optional<OmniType> findByDataType(String dataType);

    void deleteById(Long id);

    long countByDataType(String dataType);

}
