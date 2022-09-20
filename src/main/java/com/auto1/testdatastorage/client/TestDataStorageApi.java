package com.auto1.testdatastorage.client;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

}
