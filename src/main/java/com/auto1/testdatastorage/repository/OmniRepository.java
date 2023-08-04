package com.auto1.testdatastorage.repository;

import com.auto1.testdatastorage.domain.Omni;
import com.auto1.testdatastorage.domain.OmniType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OmniRepository
        extends JpaRepository<Omni, Long>, JpaSpecificationExecutor<Omni> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Omni> findFirstByOmniTypeAndArchivedOrderByIdAsc(OmniType omniType, boolean archived);

    Long countByOmniTypeAndArchived(OmniType omniType, boolean archived);

    @Transactional
    void deleteAllByOmniTypeId(Long omniTypeId);

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Omni> findAllByArchivedAndUpdatedBefore(boolean archived, LocalDateTime before);

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Omni> findAllByOmniTypeAndArchivedAndCreatedBefore(OmniType omniType, boolean archived, LocalDateTime before);

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Omni> findAllByOmniTypeAndArchivedAndUpdatedBefore(OmniType omniType, boolean archived, LocalDateTime before);

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Omni> findAllByOmniTypeAndCreatedBefore(OmniType omniType, LocalDateTime before);

    @Modifying
    @Query(
            value = "UPDATE test_data_storage.omni_queue " +
                    "SET archived = true, updated = NOW() " +
                    "WHERE omni_type_id = :dataTypeId AND created < :before",
            nativeQuery = true
    )
    int archiveByDataTypeAndCreatedBefore(Long dataTypeId, LocalDateTime before);
}
