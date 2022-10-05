package com.auto1.testdatastorage.repository;

import com.auto1.testdatastorage.domain.Omni;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OmniRepository
        extends JpaRepository<Omni, Long>, JpaSpecificationExecutor<Omni> {

    Omni findFirstByDataTypeAndArchivedOrderByIdAsc(String dataType, boolean archived);

    Long countByDataTypeAndArchived(String dataType, boolean archived);

    @Transactional
    void deleteAllByDataType(String dataType);

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE test_data_storage.omni_queue", nativeQuery = true)
    void truncate();

    @Query(value = "SELECT DISTINCT data_type FROM test_data_storage.omni_queue ORDER BY data_type ASC", nativeQuery = true)
    List<String> findDistinctDataTypes();

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Omni> findAllByArchivedAndUpdatedBefore(boolean archived, LocalDateTime before);

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Omni> findAllByDataTypeAndArchivedAndCreatedBefore(String dataType, boolean archived, LocalDateTime before);

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Omni> findAllByDataTypeAndArchivedAndUpdatedBefore(String dataType, boolean archived, LocalDateTime before);

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Omni> findAllByDataTypeAndCreatedBefore(String dataType, LocalDateTime before);
}
