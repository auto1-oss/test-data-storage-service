package com.auto1.testdatastorage.service;

import com.auto1.testdatastorage.domain.OmniType;
import com.auto1.testdatastorage.dto.OmniTypeDTO;
import com.auto1.testdatastorage.mapping.EntityMapper;
import com.auto1.testdatastorage.repository.OmniTypeRepository;
import com.auto1.testdatastorage.utils.ExceptionSupplier;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class OmniTypeService {

    private final OmniTypeRepository omniTypeRepository;

    public void createOmniType(OmniTypeDTO omniTypeDTO) {
        log.info("Create omni type [{}]", omniTypeDTO.getDataType());
        var type = omniTypeRepository.findByDataType(omniTypeDTO.getDataType());
        if (type.isEmpty()) {
            var omniType = OmniType.builder()
                    .dataType(omniTypeDTO.getDataType())
                    .meta(omniTypeDTO.getMeta())
                    .created(LocalDateTime.now())
                    .build();
            omniTypeRepository.save(omniType);
        }
    }

    public OmniTypeDTO updateOmniTypeById(Long id, OmniTypeDTO omniTypeDTO) {
        log.info("Update omni type id [{}]", id);
        var omniType = omniTypeRepository
                .findById(id)
                .orElseThrow(ExceptionSupplier.notFoundException("Omni type id", id.toString()));

        omniType.setDataType(omniTypeDTO.getDataType());
        omniType.setMeta(omniTypeDTO.getMeta());
        omniType.setUpdated(LocalDateTime.now());

        var updatedOmniType = omniTypeRepository.save(omniType);
        return EntityMapper.toOmniTypeDTO(updatedOmniType);
    }

    public void deleteOmniTypeById(Long id) {
        log.info("Delete omni type by data type [{}]", id);
        Optional.of(id)
                .filter(omniTypeRepository::existsById)
                .ifPresent(omniTypeRepository::deleteById);
        log.trace("Successfully deleted omni data type [{}]", id);
    }

    public List<OmniTypeDTO> getAllOmniTypes() {
        log.info("Get all omni types");
        var omniTypes = omniTypeRepository.findAll();
        return omniTypes.stream()
                .map(EntityMapper::toOmniTypeDTO)
                .collect(Collectors.toList());
    }
}
