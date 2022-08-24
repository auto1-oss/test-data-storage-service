package com.auto1.shift.dataprovider.service;

import com.auto1.shift.dataprovider.dto.CleanOmniDTO;
import com.auto1.shift.dataprovider.dto.OmniDTO;
import com.auto1.shift.dataprovider.dto.OmniItemCountDTO;
import com.auto1.shift.dataprovider.dto.OmniSearchDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataProviderService {

    public void createOmni(String dataType, Object omni) {

    }

    public String getOmni(String dataType) {
        return null;
    }

    public void purgeOmniUserQueue() {

    }

    public void purgeOmniTypeUserQueue(String dataType) {

    }

    public OmniItemCountDTO countOmniTypeItems(String dataType) {
        return null;
    }

    public List<OmniItemCountDTO> countOmniTypes() {
        return null;
    }

    public void deleteOmni(Long id) {

    }

    public List<OmniDTO> searchOmnis(OmniSearchDTO searchDTO) {
        return null;
    }

    public void cleanOmnis(CleanOmniDTO cleanOmniDTO) {

    }
}
