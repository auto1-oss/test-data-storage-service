package com.auto1.testdatastorage.client;

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

import com.auto1.testdatastorage.dto.OmniTypeDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "omni-type-service")
public interface OmniTypeApi {

    @ApiOperation("Create omni type")
    @PostMapping(
            value = "/queue/omni-type",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    void createOmniType(@RequestBody OmniTypeDTO omniTypeDTO);

    @ApiOperation("Update omni type")
    @PutMapping(
            value = "/queue/omni-type/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    OmniTypeDTO updateOmniTypeById(@PathVariable("id") Long id, @RequestBody OmniTypeDTO omniTypeDTO);

    @ApiOperation("Delete omni type")
    @DeleteMapping(
            value = "/queue/omni-type/{id}"
    )
    void deleteOmniTypeById(@PathVariable("id") Long id);


    @ApiOperation("Get all omni types")
    @GetMapping(
            value = "/queue/omni-types",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    List<OmniTypeDTO> getAllOmniTypes();
}
