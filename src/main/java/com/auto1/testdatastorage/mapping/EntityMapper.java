package com.auto1.testdatastorage.mapping;

import com.auto1.testdatastorage.domain.OmniQueueItem;
import com.auto1.testdatastorage.dto.OmniDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.copyProperties;

@Component
@RequiredArgsConstructor
public class EntityMapper {

    public static OmniDTO toDTO(final OmniQueueItem omniQueueItem) {
        final OmniDTO omniDTO = new OmniDTO();
        copyProperties(omniQueueItem, omniDTO);
        return omniDTO;
    }

    public static List<OmniDTO> toDTO(final List<OmniQueueItem> omniQueueItems) {
        return omniQueueItems.parallelStream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

}
