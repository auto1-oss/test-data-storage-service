package com.auto1.shiftdataprovider.domain;

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
@Table(schema = "test_data_storage", name = "omni_queue")
public class OmniQueueItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "omni_queue_sequence_generator")
    @SequenceGenerator(name = "omni_queue_sequence_generator", sequenceName = "test_data_storage.omni_queue_sequence")
    private Long id;
    private String data;
    private String dataType;
    private Boolean dirty;
    private LocalDateTime created;
    private LocalDateTime updated;

}
