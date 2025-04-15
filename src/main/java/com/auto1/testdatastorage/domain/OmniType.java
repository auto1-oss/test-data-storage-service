/*
* This file is part of the auto1-oss/test-data-storage-service.
*
* (c) AUTO1 Group SE https://www.auto1-group.com
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/

package com.auto1.testdatastorage.domain;

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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "test_data_storage", name = "omni_type")
public class OmniType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "omni_type_sequence_generator")
    @SequenceGenerator(
            name = "omni_type_sequence_generator",
            sequenceName = "test_data_storage.omni_type_sequence",
            allocationSize = 1)
    private Long id;
    private String dataType;
    private String meta;
    private LocalDateTime created;
    private LocalDateTime updated;

    public interface OmniTypeWithCount {
        Long getId();
        String getDataType();
        String getMeta();
        LocalDateTime getCreated();
        LocalDateTime getUpdated();
        Long getCount();
    }

}
