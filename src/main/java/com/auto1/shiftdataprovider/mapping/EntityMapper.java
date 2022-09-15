package com.auto1.shiftdataprovider.mapping;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public interface EntityMapper<T, DTO> {

    DTO toDTO(T entity);

    T fromDTO(DTO dto);

    T fromUpdateDTO(DTO dto);

    default List<DTO> toDTOs(Collection<T> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toUnmodifiableList());
    }

    default List<T> fromDTOs(Collection<DTO> dtos) {
        return dtos.stream().map(this::fromDTO).collect(Collectors.toUnmodifiableList());
    }

    default List<T> fromUpdateDTO(Collection<DTO> dtos) {
        return dtos.stream().map(this::fromUpdateDTO).collect(Collectors.toUnmodifiableList());
    }
}
