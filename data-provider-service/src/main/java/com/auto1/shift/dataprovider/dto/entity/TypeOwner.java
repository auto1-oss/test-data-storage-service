package com.auto1.shift.dataprovider.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "test_data_provider", name = "type_owners")
public class TypeOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "type_owners_sequence_generator")
    @SequenceGenerator(name = "type_owners_sequence_generator", sequenceName = "test_data_provider.type_owners_sequence")
    private Long id;
    private String dataType;
    @Nullable
    private String owner;

}
