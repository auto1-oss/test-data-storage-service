package com.auto1.testdatastorage.controller;

import com.auto1.testdatastorage.client.TestDataStorageApi;
import com.auto1.testdatastorage.dto.ArchiveOmniDTO;
import com.auto1.testdatastorage.dto.OmniDTO;
import com.auto1.testdatastorage.dto.OmniItemCountDTO;
import com.auto1.testdatastorage.dto.OmniSearchDTO;
import com.auto1.testdatastorage.service.TestDataStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


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
    @ResponseStatus(OK)
    public String getOmni(String dataType) {
        return testDataStorageService.getOmni(dataType);
    }

    @Override
    @ResponseStatus(OK)
    public void purgeAllByDataType(String dataType) {
        testDataStorageService.purgeAllByDataType(dataType);
    }

    @Override
    @ResponseStatus(OK)
    public void purgeAllData() {
        testDataStorageService.purgeAllData();
    }

    @Override
    public OmniItemCountDTO countOmniTypeItems(String dataType) {
        return testDataStorageService.countOmniByDataType(dataType);
    }

    @Override
    public List<OmniItemCountDTO> countAllOmni() {
        return testDataStorageService.countAllOmni();
    }

    @Override
    public void deleteOmniById(Long id) {
        testDataStorageService.deleteOmniById(id);
    }

    @Override
    public List<OmniDTO> searchOmnis(OmniSearchDTO searchDTO) {
        return testDataStorageService.searchOmnis(searchDTO);
    }

    @Override
    public void archiveOmni(ArchiveOmniDTO archiveOmniDTO) {
        testDataStorageService.archiveOmni(archiveOmniDTO);
    }

}
