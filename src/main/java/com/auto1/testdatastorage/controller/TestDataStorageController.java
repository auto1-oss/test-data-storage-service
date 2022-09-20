package com.auto1.testdatastorage.controller;

import com.auto1.testdatastorage.client.TestDataStorageApi;
import com.auto1.testdatastorage.service.TestDataStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;


@RestController
@RequiredArgsConstructor
@RequestMapping("v1/")
public class TestDataStorageController implements TestDataStorageApi {

    private final TestDataStorageService testDataStorageService;

    @Override
    @ResponseStatus(CREATED)
    public void createOmni(String dataType, String omni) {
        testDataStorageService.createOmni(dataType, omni);
    }

    @Override
    public String getOmni(String dataType) {
        return testDataStorageService.getOmni(dataType);
    }

}
