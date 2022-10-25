package com.auto1.testdatastorage.controller;

import com.auto1.testdatastorage.client.OmniQueueApi;
import com.auto1.testdatastorage.dto.ArchiveOmniDTO;
import com.auto1.testdatastorage.dto.OmniDTO;
import com.auto1.testdatastorage.dto.OmniItemCountDTO;
import com.auto1.testdatastorage.dto.OmniSearchDTO;
import com.auto1.testdatastorage.service.OmniQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("v1/")
public class OmniQueueController implements OmniQueueApi {

    private final OmniQueueService omniQueueService;

    @Override
    @ResponseStatus(CREATED)
    public void createOmni(String dataType, String data) {
        omniQueueService.createOmni(dataType, data);
    }

    @Override
    @ResponseStatus(OK)
    public String getOmni(String dataType) {
        return omniQueueService.getOmni(dataType);
    }

    @Override
    @ResponseStatus(OK)
    public void purgeAllByDataType(String dataType) {
        omniQueueService.purgeAllByDataType(dataType);
    }

    @Override
    public OmniItemCountDTO countOmniByDataType(String dataType) {
        return omniQueueService.countOmniByDataType(dataType);
    }

    @Override
    public List<OmniItemCountDTO> countAllOmni() {
        return omniQueueService.countAllOmni();
    }

    @Override
    public void deleteOmniById(Long id) {
        omniQueueService.deleteOmniById(id);
    }

    @Override
    public List<OmniDTO> searchOmnis(OmniSearchDTO searchDTO) {
        return omniQueueService.searchOmnis(searchDTO);
    }

    @Override
    public void archiveOmni(ArchiveOmniDTO archiveOmniDTO) {
        omniQueueService.archiveOmni(archiveOmniDTO);
    }
}
