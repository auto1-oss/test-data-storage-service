package com.auto1.testdatastorage.client;

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
