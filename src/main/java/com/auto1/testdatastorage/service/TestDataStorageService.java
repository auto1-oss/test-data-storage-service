package com.auto1.testdatastorage.service;

import com.auto1.testdatastorage.domain.OmniQueueItem;
import com.auto1.testdatastorage.domain.TypeOwner;
import com.auto1.testdatastorage.dto.ArchiveOmniDTO;
import com.auto1.testdatastorage.dto.OmniDTO;
import com.auto1.testdatastorage.dto.OmniItemCountDTO;
import com.auto1.testdatastorage.dto.OmniSearchDTO;
import com.auto1.testdatastorage.exception.EmptyQueueException;
import com.auto1.testdatastorage.mapping.EntityMapper;
import com.auto1.testdatastorage.repository.OmniRepository;
import com.auto1.testdatastorage.repository.TypeOwnersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class TestDataStorageService {

    private final OmniRepository omniRepository;
    private final TypeOwnersRepository typeOwnersRepository;

    public void createOmni(String dataType, String omni) {
        log.info("Create omni [{}] data type", dataType);
        var omniQueueItem = OmniQueueItem.builder()
                .data(omni)
                .dataType(dataType)
                .archived(false)
                .created(LocalDateTime.now())
                .build();

        omniRepository.save(omniQueueItem);
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
        Optional.ofNullable(this.typeOwnersRepository.findByDataType(dataType)
                .orElse(TypeOwner.builder().dataType(dataType).owner("N/A").build())
                .getOwner()).ifPresent(omniCount::setOwner);
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
            Optional.ofNullable(this.typeOwnersRepository.findByDataType(dataType)
                    .orElse(TypeOwner.builder().dataType(dataType).owner("N/A").build()).getOwner()).ifPresent(omniCount::setOwner);
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
        var omniQueueItems = searchOmni(searchDTO);
        return EntityMapper.toDTO(omniQueueItems);
    }

    private List<OmniQueueItem> searchOmni(OmniSearchDTO searchDTO) {
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
        var omniQueueItems = omniRepository.findAllByDataTypeAndCreatedBefore(archiveOmniDTO.getDataType(), archiveOmniDTO.getCreatedBefore());
        if (!omniQueueItems.isEmpty()) {
            omniQueueItems.forEach(omniQueueItem -> {
                omniQueueItem.setUpdated(LocalDateTime.now());
                omniQueueItem.setArchived(true);
                omniRepository.save(omniQueueItem);
            });
        } else {
            log.info("Return empty queue with data type [{}] and created date before [{}]", archiveOmniDTO.getDataType(), archiveOmniDTO.getCreatedBefore());
        }
    }
}
