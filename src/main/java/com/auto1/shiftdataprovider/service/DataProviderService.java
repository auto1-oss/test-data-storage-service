package com.auto1.shiftdataprovider.service;

import com.auto1.shiftdataprovider.domain.OmniQueueItem;
import com.auto1.shiftdataprovider.dto.OmniDTO;
import com.auto1.shiftdataprovider.mapping.EntityMapper;
import com.auto1.shiftdataprovider.repository.OmniRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class DataProviderService {

//    private final EntityMapper<OmniQueueItem, OmniDTO> entityMapper;
    private final OmniRepository omniRepository;


    public void createOmni(String dataType, String omni) {
        log.info("Create omni [{}] data type", dataType);
        var omniQueueItem = OmniQueueItem.builder()
                .dataType(dataType)
                .data(omni)
                .build();
//        var omniDTO =
//                OmniDTO.builder()
//                        .dataType(dataType)
//                        .data(omni)
//                        .build();
//        omniRepository.save(omniQueueItem);
    }

    public String getOmni(String dataType) {
        log.info("Get Omni by data type");
//        var omnies =
//                omniRepository.findAllByDataType(dataType);
//        return omnies.toString();
        return null;
    }


}
