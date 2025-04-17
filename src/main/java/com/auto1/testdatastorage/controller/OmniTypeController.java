/*
 * This file is part of the auto1-oss/test-data-storage-service.
 *
 * (c) AUTO1 Group SE https://www.auto1-group.com
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.auto1.testdatastorage.controller;

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

import com.auto1.testdatastorage.client.OmniTypeApi;
import com.auto1.testdatastorage.dto.OmniTypeDTO;
import com.auto1.testdatastorage.service.OmniTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("v1/")
public class OmniTypeController implements OmniTypeApi {

    private final OmniTypeService omniTypeService;

    @Override
    @ResponseStatus(CREATED)
    public void createOmniType(OmniTypeDTO omniTypeDTO) {
        omniTypeService.createOmniType(omniTypeDTO);
    }

    @Override
    public OmniTypeDTO updateOmniTypeById(Long id, OmniTypeDTO omniTypeDTO) {
        return omniTypeService.updateOmniTypeById(id, omniTypeDTO);
    }

    @Override
    public void deleteOmniTypeById(Long id) {
        omniTypeService.deleteOmniTypeById(id);
    }

    @Override
    public List<OmniTypeDTO> getAllOmniTypes() {
        return omniTypeService.getAllOmniTypes();
    }
}
