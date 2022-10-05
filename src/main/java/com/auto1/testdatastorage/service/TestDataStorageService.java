package com.auto1.testdatastorage.service;

import com.auto1.testdatastorage.domain.Omni;
import com.auto1.testdatastorage.domain.OmniType;
import com.auto1.testdatastorage.dto.*;
import com.auto1.testdatastorage.exception.EmptyQueueException;
import com.auto1.testdatastorage.mapping.EntityMapper;
import com.auto1.testdatastorage.repository.OmniRepository;
import com.auto1.testdatastorage.repository.OmniTypeRepository;
import com.auto1.testdatastorage.utils.ExceptionSupplier;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class TestDataStorageService {

    private final OmniRepository omniRepository;
    private final OmniTypeRepository omniTypeRepository;

    public void createOmni(String dataType, String omniData) {
        log.info("Create omni [{}] data type", dataType);
        var omni = Omni.builder()
                .data(omniData)
                .dataType(dataType)
                .archived(false)
                .created(LocalDateTime.now())
                .build();

        omniRepository.save(omni);
    }

    public String getOmni(String dataType) {
        log.info("Get omni [{}] data type", dataType);
        var omni = "";
        try {
            omni = consumeOmni(dataType);
        } catch (EmptyQueueException e) {
            log.warn("No [{}] found in the queue", dataType);
        }
        return omni;
    }

    private String consumeOmni(String dataType) throws EmptyQueueException {
        var omni = Optional
                .ofNullable(omniRepository.findFirstByDataTypeAndArchivedOrderByIdAsc(dataType, false))
                .orElseThrow(() -> new EmptyQueueException(String.format("Empty Omni Queue for type: [%s]", dataType)));

        omni.setUpdated(LocalDateTime.now());
        omni.setArchived(true);
        omniRepository.save(omni);
        return omni.getData();
    }

    public void purgeAllByDataType(String dataType) {
        log.info("Purge all data by data type [{}]", dataType);
        omniRepository.deleteAllByDataType(dataType);
    }

    public void purgeAllData() {
        log.info("Purge all data");
        omniRepository.truncate();
    }

    public OmniItemCountDTO countOmniByDataType(String dataType) {
        log.info("Count omni by [{}] data type", dataType);
        var omniCount = new OmniItemCountDTO();
        omniCount.setDataType(dataType);
        omniCount.setItemCount(omniRepository.countByDataTypeAndArchived(dataType, false));
        Optional.ofNullable(this.omniTypeRepository.findByDataType(dataType)
                .orElse(OmniType.builder().dataType(dataType).meta("N/A").build())
                .getMeta()).ifPresent(omniCount::setMeta);
        return omniCount;
    }

    public List<OmniItemCountDTO> countAllOmni() {
        log.info("Count all omni");
        List<OmniItemCountDTO> itemsCounts = new ArrayList<>();
        var dataTypes = this.omniRepository.findDistinctDataTypes();

        for (String dataType : dataTypes) {
            OmniItemCountDTO omniCount = new OmniItemCountDTO();
            omniCount.setDataType(dataType);
            omniCount.setItemCount(this.omniRepository.countByDataTypeAndArchived(dataType, false));
            Optional.ofNullable(this.omniTypeRepository.findByDataType(dataType)
                    .orElse(OmniType.builder().dataType(dataType).meta("N/A").build()).getMeta()).ifPresent(omniCount::setMeta);
            itemsCounts.add(omniCount);
        }
        return itemsCounts;
    }

    public void deleteOmniById(Long id) {
        log.info("Delete omni by id [{}]", id);
        omniRepository.deleteById(id);
    }

    public List<OmniDTO> searchOmnis(OmniSearchDTO searchDTO) {
        log.info("Searching omnis");
        var omnis = searchOmni(searchDTO);
        return EntityMapper.toOmniDTO(omnis);
    }

    private List<Omni> searchOmni(OmniSearchDTO searchDTO) {
        if (searchDTO.getDataType() == null || searchDTO.getDataType().isEmpty()) {
            return omniRepository.findAllByArchivedAndUpdatedBefore(
                    searchDTO.getArchived(), searchDTO.getUpdatedBeforeDate());
        } else if (searchDTO.getCreatedBeforeDate() != null && searchDTO.getUpdatedBeforeDate() == null) {
            return omniRepository.findAllByDataTypeAndArchivedAndCreatedBefore(searchDTO.getDataType(), searchDTO.getArchived(), searchDTO.getCreatedBeforeDate());
        } else if (searchDTO.getCreatedBeforeDate() == null && searchDTO.getUpdatedBeforeDate() != null) {
            return omniRepository.findAllByDataTypeAndArchivedAndUpdatedBefore(
                    searchDTO.getDataType(), searchDTO.getArchived(), searchDTO.getUpdatedBeforeDate());
        } else {
            log.error("Search did not return any match by [{}]", searchDTO);
            throw new NotImplementedException("Search did not return any match"); //todo check if it's needed
        }
    }

    public void archiveOmni(ArchiveOmniDTO archiveOmniDTO) {
        log.info("Archive omni by [{}] data type and created before [{}]", archiveOmniDTO.getDataType(), archiveOmniDTO.getCreatedBefore());
        var omnis = omniRepository.findAllByDataTypeAndCreatedBefore(archiveOmniDTO.getDataType(), archiveOmniDTO.getCreatedBefore());
        if (!omnis.isEmpty()) {
            omnis.forEach(omni -> {
                omni.setUpdated(LocalDateTime.now());
                omni.setArchived(true);
                omniRepository.save(omni);
            });
        } else {
            log.info("Return empty queue with data type [{}] and created date before [{}]", archiveOmniDTO.getDataType(), archiveOmniDTO.getCreatedBefore());
        }
    }

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
        var omniType =
                omniTypeRepository
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
        omniTypeRepository.deleteById(id);
    }

    public List<OmniTypeDTO> getAllOmniTypes() {
        log.info("Get all omni types");
        var omniTypes = omniTypeRepository.findAll();
        return omniTypes.stream()
                .map(EntityMapper::toOmniTypeDTO)
                .collect(Collectors.toList());
    }
}
