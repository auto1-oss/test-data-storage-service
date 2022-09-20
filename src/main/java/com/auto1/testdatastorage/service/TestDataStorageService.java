package com.auto1.testdatastorage.service;

import com.auto1.testdatastorage.domain.OmniQueueItem;
import com.auto1.testdatastorage.exception.EmptyQueueException;
import com.auto1.testdatastorage.repository.OmniRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class TestDataStorageService {

    private final OmniRepository omniRepository;

    public void createOmni(String dataType, String omni) {
        log.info("Create omni [{}] data type", dataType);
        var omniQueueItem = OmniQueueItem.builder()
                .data(omni)
                .dataType(dataType)
                .dirty(false)
                .created(LocalDateTime.now())
                .build();

        omniRepository.save(omniQueueItem);
    }

    public String getOmni(String dataType) {
        log.info("Get omni [{}] data type", dataType);
        String omni = "";
        try {
            omni = consumeOmni(dataType);
        } catch (EmptyQueueException e) {
            log.warn("No [{}] found in the queue", dataType);
        }
        return omni;
    }

    private String consumeOmni(String dataType) throws EmptyQueueException {
        OmniQueueItem omni = Optional
                .ofNullable(omniRepository.findFirstByDataTypeAndDirtyOrderByIdAsc(dataType, false))
                .orElseThrow(() -> new EmptyQueueException(String.format("Empty Omni Queue for type: [%s]", dataType)));

        omni.setUpdated(LocalDateTime.now());
        omni.setDirty(true);
        omniRepository.save(omni);
        return omni.getData();
    }
}
