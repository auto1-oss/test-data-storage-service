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

import com.auto1.testdatastorage.dto.ArchiveOmniDTO;
import com.auto1.testdatastorage.dto.OmniDTO;
import com.auto1.testdatastorage.dto.OmniItemCountDTO;
import com.auto1.testdatastorage.dto.OmniSearchDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "omni-queue-service")
public interface OmniQueueApi {

    @ApiOperation("Create omni")
    @PostMapping(
            value = "/queue/omni/{data-type}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    void createOmni(@PathVariable("data-type") String dataType, @RequestBody String data);

    @ApiOperation("Get omni")
    @GetMapping(
            value = "/queue/omni/{data-type}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    String getOmni(@PathVariable("data-type") String dataType);

    @ApiOperation("Purge all data by data type")
    @PostMapping(
            value = "/queue/omni/{data-type}/purge",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    void purgeAllByDataType(@PathVariable("data-type") String dataType);

    @ApiOperation("Count omni by data type")
    @GetMapping(
            value = "/queue/omni/{data-type}/count",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    OmniItemCountDTO countOmniByDataType(@PathVariable("data-type") String dataType);

    @ApiOperation("Count all omni")
    @GetMapping(
            value = "/queue/omni/count",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    List<OmniItemCountDTO> countAllOmni();

    @ApiOperation("Delete omni by id")
    @DeleteMapping(
            value = "/queue/omni/{id}"
    )
    void deleteOmniById(@PathVariable("id") Long id);

    @ApiOperation("Search omnis")
    @PostMapping(
            value = "/queue/omni/search",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    List<OmniDTO> searchOmnis(@RequestBody OmniSearchDTO searchDTO);

    @ApiOperation("Archive omni queue")
    @PostMapping(
            value = "/queue/omni/clean",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    void archiveOmni(@RequestBody ArchiveOmniDTO archiveOmniDTO);
}
