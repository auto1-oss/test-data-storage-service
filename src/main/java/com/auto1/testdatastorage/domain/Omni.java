package com.auto1.testdatastorage.domain;

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
