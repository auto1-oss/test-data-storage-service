package com.auto1.testdatastorage.mapping;

import com.auto1.testdatastorage.domain.Omni;
import com.auto1.testdatastorage.domain.OmniType;
import com.auto1.testdatastorage.dto.OmniDTO;
import com.auto1.testdatastorage.dto.OmniTypeDTO;
import lombok.experimental.UtilityClass;

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

    public static OmniType toOmniType(final OmniTypeDTO omniTypeDTO) {
        final OmniType omniType = new OmniType();
        copyProperties(omniTypeDTO, omniType);
        return omniType;
    }

    public static List<OmniTypeDTO> toOmniTypeDTO(final List<OmniType> omniTypes) {
        return omniTypes.parallelStream().map(EntityMapper::toOmniTypeDTO).collect(Collectors.toList());
    }

}
