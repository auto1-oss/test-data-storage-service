package com.auto1.testdatastorage.mapping;

import com.auto1.testdatastorage.domain.Omni;
import com.auto1.testdatastorage.domain.OmniType;
import com.auto1.testdatastorage.dto.OmniDTO;
import com.auto1.testdatastorage.dto.OmniTypeDTO;
import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.copyProperties;

@Component
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

    public static List<OmniTypeDTO> toOmniTypeDTO(final List<OmniType> omniTypes) {
        return omniTypes.parallelStream().map(EntityMapper::toOmniTypeDTO).collect(Collectors.toList());
    }

}
