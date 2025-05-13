/*
 * This file is part of the auto1-oss/test-data-storage-service.
 *
 * (c) AUTO1 Group SE https://www.auto1-group.com
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.auto1.testdatastorage.service;

/*-
 * #%L
 * test-data-storage-service
 * %%
 * Copyright (C) 2023 Auto1 Group
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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

    @Transactional
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
        omniRepository.deleteAllByOmniTypeIdAndArchived(omniType.getId(), false);
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
