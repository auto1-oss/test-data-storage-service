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

    @Query(value = "SELECT " +
            "    id, " +
            "    data_type AS dataType, " +
            "    meta, " +
            "    created, " +
            "    updated, " +
            "    COALESCE(count, 0) AS count " +
            "FROM test_data_storage.omni_type " +
            "LEFT JOIN ( " +
            "    SELECT omni_type_id, COUNT(*) AS count " +
            "    FROM test_data_storage.omni_queue " +
            "    WHERE archived = false " +
            "    GROUP BY omni_type_id " +
            ") AS subquery ON omni_type.id = subquery.omni_type_id " +
            "ORDER BY test_data_storage.omni_type.id ASC", nativeQuery = true)
    List<OmniType.OmniTypeWithCount> findAllAndCount();
}
