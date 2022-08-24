package com.auto1.shift.dataprovider.controller;

import com.auto1.shift.dataprovider.client.DataProviderApi;
import com.auto1.shift.dataprovider.dto.CleanOmniDTO;
import com.auto1.shift.dataprovider.dto.OmniDTO;
import com.auto1.shift.dataprovider.dto.OmniItemCountDTO;
import com.auto1.shift.dataprovider.dto.OmniSearchDTO;
import com.auto1.shift.dataprovider.service.DataProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;


@RestController
@RequiredArgsConstructor
@RequestMapping("/data-provider")
public class DataProviderController implements DataProviderApi {

    private final DataProviderService dataProviderService;

    @Override
    @ResponseStatus(CREATED)
    public void createOmni(String dataType, Object omni) {
        dataProviderService.createOmni(dataType, omni);
    }

    @Override
    public String getOmni(String dataType) {
        return null;
    }

    @Override
    public void purgeOmniUserQueue() {

    }

    @Override
    public void purgeOmniTypeUserQueue(String dataType) {

    }

    @Override
    public OmniItemCountDTO countOmniTypeItems(String dataType) {
        return null;
    }

    @Override
    public List<OmniItemCountDTO> countOmniTypes() {
        return null;
    }

    @Override
    public void deleteOmni(Long id) {

    }

    @Override
    public List<OmniDTO> searchOmnis(OmniSearchDTO searchDTO) {
        return null;
    }

    @Override
    public void cleanOmnis(CleanOmniDTO cleanOmniDTO) {

    }
}
