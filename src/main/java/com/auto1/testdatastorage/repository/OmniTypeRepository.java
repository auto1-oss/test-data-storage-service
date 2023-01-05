package com.auto1.testdatastorage.repository;

import com.auto1.testdatastorage.domain.OmniType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OmniTypeRepository
        extends JpaRepository<OmniType, Long>, JpaSpecificationExecutor<OmniType> {

    Optional<OmniType> findByDataType(String dataType);

    List<OmniType> findAllByOrderByIdAsc();

    void deleteById(Long id);

    @Query(value = "SELECT DISTINCT data_type FROM test_data_storage.omni_type ORDER BY data_type ASC", nativeQuery = true)
    List<String> findDistinctDataTypes();

}
