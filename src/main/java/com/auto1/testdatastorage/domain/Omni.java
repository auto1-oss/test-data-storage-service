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

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "test_data_storage", name = "omni_queue")
public class Omni {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "omni_queue_sequence_generator")
    @SequenceGenerator(
            name = "omni_queue_sequence_generator",
            sequenceName = "test_data_storage.omni_queue_sequence",
            allocationSize = 1)
    private Long id;
    private String data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "omni_type_id")
    private OmniType omniType;

    private Boolean archived;
    private LocalDateTime created;
    private LocalDateTime updated;
}
