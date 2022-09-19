package com.auto1.testdatastorage.service;

import com.auto1.testdatastorage.domain.OmniQueueItem;
import com.auto1.testdatastorage.repository.OmniRepository;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
                .updated(LocalDateTime.now())
                .build();

        omniRepository.save(omniQueueItem);
    }

    public String getOmni(String dataType) {
        log.info("Get Omni by data type");
        var omnies =
                omniRepository.findAllByDataType(dataType);
        return new Gson().toJson(omnies);
    }


}
