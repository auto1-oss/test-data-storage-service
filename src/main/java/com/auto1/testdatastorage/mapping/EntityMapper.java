package com.auto1.testdatastorage.mapping;

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
import com.auto1.testdatastorage.dto.OmniDTO;
import com.auto1.testdatastorage.dto.OmniItemCountDTO;
import com.auto1.testdatastorage.dto.OmniTypeDTO;
import lombok.experimental.UtilityClass;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.copyProperties;

@UtilityClass
public class EntityMapper {

    public static OmniDTO toOmniDTO(final Omni omni) {
        final OmniDTO omniDTO = new OmniDTO();
        copyProperties(omni, omniDTO);
        return omniDTO;
    }

    public static List<OmniDTO> toOmniDTO(final List<Omni> omnis) {
        return omnis.parallelStream().map(EntityMapper::toOmniDTO).collect(Collectors.toList());
    }

    public static OmniTypeDTO toOmniTypeDTO(final OmniType omniType) {
        final OmniTypeDTO omniTypeDTO = new OmniTypeDTO();
        copyProperties(omniType, omniTypeDTO);
        return omniTypeDTO;
    }

    public static OmniTypeDTO toOmniTypeDTO(final OmniType.OmniTypeWithCount omniType) {
        final OmniTypeDTO omniTypeDTO = new OmniTypeDTO();
        copyProperties(omniType, omniTypeDTO);
        return omniTypeDTO;
    }

    public static OmniItemCountDTO toOmniItemCountDTO(final OmniType.OmniTypeWithCount omniType) {
        return OmniItemCountDTO.builder()
                .dataType(omniType.getDataType())
                .itemCount(omniType.getCount())
                .meta(omniType.getMeta())
                .build();
    }

    public static OmniItemCountDTO toOmniItemCountDTO(final OmniType omniType, long count) {
        return OmniItemCountDTO.builder()
                .dataType(omniType.getDataType())
                .itemCount(count)
                .meta(omniType.getMeta())
                .build();
    }

    public static OmniType toOmniType(final OmniTypeDTO omniTypeDTO) {
        final OmniType omniType = new OmniType();
        copyProperties(omniTypeDTO, omniType);
        return omniType;
    }

    public static List<OmniTypeDTO> toOmniTypeDTO(final List<OmniType> omniTypes) {
        return omniTypes.parallelStream().map(EntityMapper::toOmniTypeDTO)
                .sorted(Comparator.comparing(OmniTypeDTO::getDataType))
                .collect(Collectors.toList());
    }

}
