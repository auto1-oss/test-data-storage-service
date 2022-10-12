package com.auto1.testdatastorage.domain;

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

}
