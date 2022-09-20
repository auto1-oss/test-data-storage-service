package com.auto1.testdatastorage.mapping;

import com.auto1.testdatastorage.domain.OmniQueueItem;
import com.auto1.testdatastorage.dto.OmniDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.DataInput;

@Component
@RequiredArgsConstructor
public class EntityMapper {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public OmniDTO toOmniDTO(OmniQueueItem omniQueueItem) {
        return objectMapper.readValue((DataInput) omniQueueItem, OmniDTO.class);
    }

    @SneakyThrows
    public OmniQueueItem toOmniItem(OmniDTO omniDTO) {
        return objectMapper.readValue((DataInput) omniDTO, OmniQueueItem.class);
    }

}
