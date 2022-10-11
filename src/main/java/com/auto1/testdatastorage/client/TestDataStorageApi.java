package com.auto1.testdatastorage.client;

import com.auto1.testdatastorage.dto.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "test-data-storage-service")
public interface TestDataStorageApi {

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
    OmniTypeDTO updateOmniTypeById(@PathVariable("id") Long id, @RequestBody OmniTypeDTO testStoryDTO);

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
