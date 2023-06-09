package com.auto1.testdatastorage.service;

import com.auto1.testdatastorage.domain.Omni;
import com.auto1.testdatastorage.domain.OmniType;
import com.auto1.testdatastorage.dto.*;
import com.auto1.testdatastorage.mapping.EntityMapper;
import com.auto1.testdatastorage.repository.OmniRepository;
import com.auto1.testdatastorage.repository.OmniTypeRepository;
import com.auto1.testdatastorage.utils.ExceptionSupplier;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class OmniQueueService {

    private final OmniRepository omniRepository;
    private final OmniTypeRepository omniTypeRepository;

    public void createOmni(String dataType, String data) {
        log.info("Create omni [{}] data type", dataType);
        var omniType = getOmniTypeIfExists(dataType);
        var omni = Omni.builder()
                .data(data)
                .omniType(omniType)
                .archived(false)
                .created(LocalDateTime.now())
                .build();
        omniRepository.save(omni);
    }

    public String getOmni(String dataType) {
        log.info("Get omni [{}] data type", dataType);
        var omniType = getOmniTypeIfExists(dataType);
        var omni = omniRepository.findFirstByOmniTypeAndArchivedOrderByIdAsc(omniType, false)
                .orElseThrow(ExceptionSupplier.emptyQueueException(dataType));
        archiveOmni(omni);
        return omni.getData();
    }

    private void archiveOmni(Omni omni) {
        omni.setUpdated(LocalDateTime.now());
        omni.setArchived(true);
        omniRepository.save(omni);
    }

    public void purgeAllByDataType(String dataType) {
        log.info("Purge all data by data type [{}]", dataType);
        var omniType = getOmniTypeIfExists(dataType);
        omniRepository.deleteAllByOmniTypeId(omniType.getId());
    }

    @Transactional(readOnly = true)
    public OmniItemCountDTO countOmniByDataType(String dataType) {
        log.info("Count omni by [{}] data type", dataType);
        var omniType = getOmniTypeIfExists(dataType);
        var count = omniRepository.countByOmniTypeAndArchived(omniType, false);
        return EntityMapper.toOmniItemCountDTO(omniType, count);
    }

    @Transactional(readOnly = true)
    public List<OmniItemCountDTO> countAllOmni() {
        log.info("Count all omni");
        return omniTypeRepository.findAllAndCount().stream()
                .map(EntityMapper::toOmniItemCountDTO)
                .sorted(Comparator.comparing(OmniItemCountDTO::getDataType))
                .collect(Collectors.toList());
    }

    public void deleteOmniById(Long id) {
        log.info("Delete omni by id [{}]", id);
        omniRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<OmniDTO> searchOmnis(OmniSearchDTO searchDTO) {
        log.info("Searching omnis");
        var omnis = searchOmni(searchDTO);
        return EntityMapper.toOmniDTO(omnis);
    }

    private List<Omni> searchOmni(OmniSearchDTO searchDTO) {
        OmniType omniType;
        if (searchDTO.getDataType() == null || searchDTO.getDataType().isEmpty()) {
            return omniRepository.findAllByArchivedAndUpdatedBefore(
                    searchDTO.getArchived(), searchDTO.getUpdatedBeforeDate());
        } else if (searchDTO.getCreatedBeforeDate() != null && searchDTO.getUpdatedBeforeDate() == null) {
            omniType = getOmniTypeIfExists(searchDTO.getDataType());
            return omniRepository.findAllByOmniTypeAndArchivedAndCreatedBefore(
                    omniType, searchDTO.getArchived(), searchDTO.getCreatedBeforeDate());
        } else if (searchDTO.getCreatedBeforeDate() == null && searchDTO.getUpdatedBeforeDate() != null) {
            omniType = getOmniTypeIfExists(searchDTO.getDataType());
            return omniRepository.findAllByOmniTypeAndArchivedAndUpdatedBefore(
                    omniType, searchDTO.getArchived(), searchDTO.getUpdatedBeforeDate());
        } else {
            log.error("Search did not return any match by [{}]", searchDTO);
            throw new NotImplementedException("Search did not return any match"); //todo check if it's needed
        }
    }

    @Transactional
    public void archiveOmni(ArchiveOmniDTO archiveOmniDTO) {
        var omniType = getOmniTypeIfExists(archiveOmniDTO.getDataType());
        var archived = omniRepository
                .archiveByDataTypeAndCreatedBefore(omniType.getId(), archiveOmniDTO.getCreatedBefore());
        log.info("Archived data type items: [{}] - [{}]", archiveOmniDTO.getDataType(), archived);
    }

    private OmniType getOmniTypeIfExists(String dataType) {
        return omniTypeRepository
                .findByDataType(dataType)
                .orElseThrow(ExceptionSupplier.notFoundException("Omni type", dataType));
    }
}
