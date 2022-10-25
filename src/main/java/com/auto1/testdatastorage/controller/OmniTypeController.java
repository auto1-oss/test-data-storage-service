package com.auto1.testdatastorage.controller;

import com.auto1.testdatastorage.client.OmniTypeApi;
import com.auto1.testdatastorage.dto.OmniTypeDTO;
import com.auto1.testdatastorage.service.OmniTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/")
public class OmniTypeController implements OmniTypeApi {

    private final OmniTypeService omniTypeService;

    @Override
    @ResponseStatus(CREATED)
    public void createOmniType(OmniTypeDTO omniTypeDTO) {
        omniTypeService.createOmniType(omniTypeDTO);
    }

    @Override
    public OmniTypeDTO updateOmniTypeById(Long id, OmniTypeDTO omniTypeDTO) {
        return omniTypeService.updateOmniTypeById(id, omniTypeDTO);
    }

    @Override
    public void deleteOmniTypeById(Long id) {
        omniTypeService.deleteOmniTypeById(id);
    }

    @Override
    public List<OmniTypeDTO> getAllOmniTypes() {
        return omniTypeService.getAllOmniTypes();
    }
}
