package com.auto1.testdatastorage.controller;

import com.auto1.testdatastorage.client.TestDataStorageApi;
import com.auto1.testdatastorage.service.TestDataStorageService;
import com.google.gson.Gson;
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
    private final Gson gson;

    @Override
    @ResponseStatus(CREATED)
    public void createOmni(String dataType, Object omni) {
        String omniString = gson.toJson(omni);
        testDataStorageService.createOmni(dataType, omniString);
    }

    @Override
    public String getOmni(String dataType) {
        return testDataStorageService.getOmni(dataType);
    }

}
