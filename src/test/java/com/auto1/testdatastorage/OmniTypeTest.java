package com.auto1.testdatastorage;

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


import com.auto1.testdatastorage.domain.OmniType;
import com.auto1.testdatastorage.dto.OmniTypeDTO;
import com.auto1.testdatastorage.repository.OmniRepository;
import com.auto1.testdatastorage.repository.OmniTypeRepository;
import com.auto1.testdatastorage.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(classes = TestDataStorageApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class OmniTypeTest {

    @Autowired
    private OmniTypeRepository omniTypeRepository;
    @Autowired
    private OmniRepository omniRepository;

    @Value("http://localhost:${local.server.port}/v1")
    private String baseUrl;
    @Value("${basic.username}")
    private String username;
    @Value("${basic.password}")
    private String password;

    @BeforeEach
    public void setup() {
        this.omniTypeRepository.deleteAll();
        this.omniRepository.deleteAll();
    }

    @Test
    public void createOmniType() {
        var omniType = OmniType.builder()
                .dataType("Test data")
                .meta("Test meta")
                .created(LocalDateTime.now())
                .build();

        //@formatter:off
        given()
                .config(TestUtils.getConfig())
                .headers(TestUtils.getHeaders())
                .auth().basic(username, password)
                .baseUri(baseUrl)
                .basePath("/queue/omni-type")
                .body(omniType)
        .when()
                .post()
                .prettyPeek()
        .then()
                .statusCode(201);
        //@formatter:on

        assertThat(this.omniTypeRepository.count(), is(1L));
    }

    @Test
    public void updateOmniType() {
        var dataType = "Test data";
        var meta = "Meta";
        var omniType = OmniType.builder()
                .dataType(dataType)
                .meta(meta)
                .created(LocalDateTime.now())
                .build();

        var omni = omniTypeRepository.save(omniType);

        var dataTypeUpdated = "Test data updated";
        var metaUpdated = "Meta updated";
        var omniTypeDTO =
                OmniTypeDTO.builder()
                        .dataType(dataTypeUpdated)
                        .meta(metaUpdated)
                        .build();

        //@formatter:off
        var result =
                given()
                        .config(TestUtils.getConfig())
                        .headers(TestUtils.getHeaders())
                        .auth().basic(username, password)
                        .baseUri(baseUrl)
                        .basePath(String.format("/queue/omni-type/%s", omni.getId()))
                        .body(omniTypeDTO)
                .when()
                        .put()
                        .prettyPeek()
                .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(OmniTypeDTO.class);
        //@formatter:on

        assertThat(this.omniTypeRepository.count(), is(1L));

        var updatedOmni = this.omniTypeRepository.findById(omni.getId());
        assertThat(updatedOmni.get().getDataType(), is(result.getDataType()));
        assertThat(updatedOmni.get().getMeta(), is(result.getMeta()));
    }

    @Test
    public void updateNonExistingOmniType() {
        var dataType = "Test data";
        var meta = "Meta";
        var id = 1L;
        var omniTypeDTO =
                OmniTypeDTO.builder()
                        .dataType(dataType)
                        .meta(meta)
                        .build();

        //@formatter:off
        given()
                .config(TestUtils.getConfig())
                .headers(TestUtils.getHeaders())
                .auth().basic(username, password)
                .baseUri(baseUrl)
                .basePath(String.format("/queue/omni-type/%s", id))
                .body(omniTypeDTO)
        .when()
                .put()
                .prettyPeek()
        .then()
                .statusCode(404);
        //@formatter:on

        assertThat(this.omniTypeRepository.count(), is(0L));
    }

    @Test
    public void deleteOmniTypeById() {
        var dataType1 = "data type 1";
        var dataType2 = "data type 2";
        var omniType1 = TestUtils.buildOmniTypeItem(dataType1, "omni type 1");
        var omniType2 = TestUtils.buildOmniTypeItem(dataType2, "omni type 2");
        var omni = TestUtils.buildOmniItem(omniType1, dataType2, false);

        this.omniTypeRepository.saveAll(Arrays.asList(omniType1, omniType2));
        this.omniRepository.save(omni);

        assertThat(this.omniTypeRepository.count(), is(2L));
        assertThat(this.omniRepository.count(), is(1L));

        var omniTypeId = this.omniTypeRepository.findByDataType(dataType1).get().getId();

        //@formatter:off
        given()
                .baseUri(baseUrl)
                .basePath(String.format("/queue/omni-type/%s", omniTypeId))
                .auth().basic(username, password)
        .when()
                .delete()
                .prettyPeek()
        .then()
                .statusCode(200);
        //@formatter:on

        assertThat(this.omniTypeRepository.count(), is(1L));
        assertThat(this.omniRepository.count(), is(0L));
        assertThat(this.omniTypeRepository.findById(omniTypeId), is(Optional.empty()));
    }

    @Test
    public void getAllOmniTypes() {
        var dataType1 = "data type 1";
        var dataType2 = "data type 2";
        var omniType1 = TestUtils.buildOmniTypeItem(dataType1, "omni type 1");
        var omniType2 = TestUtils.buildOmniTypeItem(dataType2, "omni type 2");

        this.omniTypeRepository.saveAll(Arrays.asList(omniType1, omniType2));

        assertThat(this.omniTypeRepository.count(), is(2L));

        //@formatter:off
        var result =
                given()
                        .baseUri(baseUrl)
                        .basePath("/queue/omni-types")
                        .auth().basic(username, password)
                .when()
                        .get()
                        .prettyPeek()
                .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .jsonPath()
                        .getList("", OmniTypeDTO.class);
        //@formatter:on

        assertThat(result.size(), is(2));
    }
    @Test
    public void getAllOmniTypesSorted() {
        var dataType1 = "RETAIL_AD_DE";
        var dataType2 = "RETAIL_AD_ALL_DETAILS_DE";
        var dataType3 = "EVA_FINISHED_IOS";
        var omniType1 = TestUtils.buildOmniTypeItem(dataType1, "RETAIL_AD_DE");
        var omniType2 = TestUtils.buildOmniTypeItem(dataType2, "RETAIL_AD_ALL_DETAILS_DE");
        var omniType3 = TestUtils.buildOmniTypeItem(dataType3, "EVA_FINISHED_IOS");

        this.omniTypeRepository.saveAll(Arrays.asList(omniType1, omniType2, omniType3));

        assertThat(this.omniTypeRepository.count(), is(3L));

        //@formatter:off
        var result =
                given()
                        .baseUri(baseUrl)
                        .basePath("/queue/omni-types")
                        .auth().basic(username, password)
                        .when()
                        .get()
                        .prettyPeek()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .jsonPath()
                        .getList("", OmniTypeDTO.class);
        //@formatter:on

        assertThat(result.size(), is(3));
        assertThat(result.get(0).getDataType(), is ("EVA_FINISHED_IOS"));
        assertThat(result.get(1).getDataType(), is ("RETAIL_AD_ALL_DETAILS_DE"));
        assertThat(result.get(2).getDataType(), is ("RETAIL_AD_DE"));
    }


}
