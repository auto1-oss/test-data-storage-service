package com.auto1.testdatastorage.repository;

import com.auto1.testdatastorage.domain.OmniQueueItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OmniRepository
        extends JpaRepository<OmniQueueItem, Long>, JpaSpecificationExecutor<OmniQueueItem> {

    OmniQueueItem findFirstByDataTypeAndUsedOrderByIdAsc(String dataType, boolean used);

    Long countByDataTypeAndUsed(String dataType, boolean used);

    @Transactional
    void deleteAllByDataType(String dataType);

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE test_data_storage.omni_queue", nativeQuery = true)
    void truncate();

    @Query(value = "SELECT DISTINCT data_type FROM test_data_storage.omni_queue ORDER BY data_type ASC", nativeQuery = true)
    List<String> findDistinctDataTypes();

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<OmniQueueItem> findAllByUsedAndUpdatedBefore(boolean used, LocalDateTime before);

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<OmniQueueItem> findAllByDataTypeAndUsedAndCreatedBefore(String dataType, boolean used, LocalDateTime before);

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<OmniQueueItem> findAllByDataTypeAndUsedAndUpdatedBefore(String dataType, boolean used, LocalDateTime before);

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<OmniQueueItem> findAllByDataTypeAndCreatedBefore(String dataType, LocalDateTime before);
}
