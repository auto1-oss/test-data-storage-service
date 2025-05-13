/*
 * This file is part of the auto1-oss/test-data-storage-service.
 *
 * (c) AUTO1 Group SE https://www.auto1-group.com
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.auto1.testdatastorage.repository;

/*-
 * #%L
 * test-data-storage-service
 * %%
 * Copyright (C) 2023 Auto1 Group
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.auto1.testdatastorage.domain.Omni;
import com.auto1.testdatastorage.domain.OmniType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OmniRepository
        extends JpaRepository<Omni, Long>, JpaSpecificationExecutor<Omni> {

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Omni> findFirstByOmniTypeAndArchivedOrderByIdAsc(OmniType omniType, boolean archived);

    Long countByOmniTypeAndArchived(OmniType omniType, boolean archived);

    @Transactional
    void deleteAllByOmniTypeIdAndArchived(Long omniTypeId, boolean archived);

    @Transactional
    List<Omni> findAllByArchivedAndUpdatedBefore(boolean archived, LocalDateTime before);

    @Transactional
    List<Omni> findAllByOmniTypeAndArchivedAndCreatedBefore(OmniType omniType, boolean archived, LocalDateTime before);

    @Transactional
    List<Omni> findAllByOmniTypeAndArchivedAndUpdatedBefore(OmniType omniType, boolean archived, LocalDateTime before);

    @Transactional
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
