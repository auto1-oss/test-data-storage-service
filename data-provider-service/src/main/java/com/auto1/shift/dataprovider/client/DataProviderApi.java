package com.auto1.shift.dataprovider.client;

import com.auto1.shift.dataprovider.dto.CleanOmniDTO;
import com.auto1.shift.dataprovider.dto.OmniDTO;
import com.auto1.shift.dataprovider.dto.OmniItemCountDTO;
import com.auto1.shift.dataprovider.dto.OmniSearchDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "data-provider-service")
public interface DataProviderApi {

    @ApiOperation("Create omni")
    @PostMapping(
            value = "/queue/omni/{data-type}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    void createOmni(@PathVariable("data-type") String dataType, @RequestBody Object omni);

    @ApiOperation("Get omni")
    @GetMapping(
            value = "/queue/omni/{data-type}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    String getOmni(@PathVariable("data-type") String dataType);

    @ApiOperation("Purge omni queue")
    @PostMapping(
            value = "/queue/omni/purge",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    void purgeOmniUserQueue();

    @ApiOperation("Purge omni type queue")
    @PostMapping(
            value = "/queue/omni/{data-type}/purge",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    void purgeOmniTypeUserQueue(@PathVariable("data-type") String dataType);

    @ApiOperation("Count omni type items")
    @GetMapping(
            value = "/queue/omni/{data-type}/count",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    OmniItemCountDTO countOmniTypeItems(@PathVariable("data-type") String dataType);

    @ApiOperation("Count omni types")
    @GetMapping(
            value = "/queue/omni/count",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    List<OmniItemCountDTO> countOmniTypes();

    @ApiOperation("Delete omni")
    @DeleteMapping(
            value = "/queue/omni/{id}"
    )
    void deleteOmni(@PathVariable("id") Long id);

    @ApiOperation("Search omni")
    @PostMapping(
            value = "/queue/omni/search",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    List<OmniDTO> searchOmnis(@RequestBody OmniSearchDTO searchDTO);

    @ApiOperation("Clean omni queue")
    @PostMapping(
            value = "/queue/omni/clean",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    void cleanOmnis(@RequestBody CleanOmniDTO cleanOmniDTO);
}
