package com.auto1.testdatastorage.client;

import com.auto1.testdatastorage.dto.CleanOmniDTO;
import com.auto1.testdatastorage.dto.OmniDTO;
import com.auto1.testdatastorage.dto.OmniItemCountDTO;
import com.auto1.testdatastorage.dto.OmniSearchDTO;
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
    void createOmni(@PathVariable("data-type") String dataType, @RequestBody String omni);

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

    @ApiOperation("Purge all data")
    @PostMapping(
            value = "/queue/omni/purge",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    void purgeAllData();

    @ApiOperation("Count omni by data type")
    @GetMapping(
            value = "/queue/omni/{data-type}/count",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    OmniItemCountDTO countOmniTypeItems(@PathVariable("data-type") String dataType);

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

    @ApiOperation("Clean omni queue")
    @PostMapping(
            value = "/queue/omni/clean",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    void cleanOmni(@RequestBody CleanOmniDTO cleanOmniDTO);
}
