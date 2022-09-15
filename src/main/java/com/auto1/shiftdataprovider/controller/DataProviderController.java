package com.auto1.shiftdataprovider.controller;

import com.auto1.shiftdataprovider.client.DataProviderApi;
import com.auto1.shiftdataprovider.service.DataProviderService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;


@RestController
@RequiredArgsConstructor
@RequestMapping("v1/data-provider")
public class DataProviderController implements DataProviderApi {

    private final DataProviderService dataProviderService;
    private final Gson gson;

    @Override
    @ResponseStatus(CREATED)
    public void createOmni(String dataType, Object omni) {
        String omniString = gson.toJson(omni);
        dataProviderService.createOmni(dataType, omniString);
    }

    @Override
    public String getOmni(String dataType) {
        return dataProviderService.getOmni(dataType);
    }

}
