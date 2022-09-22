package com.auto1.testdatastorage.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "test_data_storage", name = "type_owners")
public class TypeOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "type_owners_sequence_generator")
    @SequenceGenerator(name = "type_owners_sequence_generator", sequenceName = "test_data_storage.type_owners_sequence")
    private Long id;
    private String dataType;
    private String owner;

}
