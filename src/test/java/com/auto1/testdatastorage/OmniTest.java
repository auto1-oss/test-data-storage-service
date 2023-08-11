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
import com.auto1.testdatastorage.dto.ArchiveOmniDTO;
import com.auto1.testdatastorage.dto.OmniDTO;
import com.auto1.testdatastorage.dto.OmniSearchDTO;
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

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@SpringBootTest(classes = TestDataStorageApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class OmniTest {

    @Autowired
    private OmniRepository omniRepository;
    @Autowired
    private OmniTypeRepository omniTypeRepository;

    @Value("http://localhost:${local.server.port}/v1")
    private String baseUrl;
    @Value("${basic.username}")
    private String username;
    @Value("${basic.password}")
    private String password;

    private final String dataType1 = "data type 1";
    private final String dataType2 = "data type 2";
    private final OmniType omniType1 = TestUtils.buildOmniTypeItem(dataType1);
    private final OmniType omniType2 = TestUtils.buildOmniTypeItem(dataType2);

    @BeforeEach
    public void setup() {
        this.omniRepository.deleteAll();
        this.omniTypeRepository.deleteAll();
        this.omniTypeRepository.save(omniType1);
        this.omniTypeRepository.save(omniType2);
    }

    @Test
    public void createOmni() {
        var omni1 = TestUtils.buildOmniItem(omniType1, "Omni 1", false);

        //@formatter:off
        given()
                .config(TestUtils.getConfig())
                .headers(TestUtils.getHeaders())
                .baseUri(baseUrl)
                .basePath(String.format("/queue/omni/%s", dataType1))
                .auth().basic(username, password)
                .body(omni1)
        .when()
                .post()
                .prettyPeek()
        .then()
                .statusCode(201);
        //@formatter:on

        assertThat(this.omniRepository.count(), is(1L));

        var omni = this.omniRepository.findFirstByOmniTypeAndArchivedOrderByIdAsc(omniType1, false);
        assertThat(omni.isPresent(), is(true));
    }

    @Test
    public void getOmni() {
        var omni1 = TestUtils.buildOmniItem(omniType1, "Omni 2", false);
        var omni2 = TestUtils.buildOmniItem(omniType2, "Omni 3", false);
        var omni3 = TestUtils.buildOmniItem(omniType1, "Omni 4", false);

        this.omniRepository.saveAll(Arrays.asList(omni1, omni2, omni3));

        //@formatter:off
        given()
                .baseUri(baseUrl)
                .basePath(String.format("/queue/omni/%s", dataType1))
                .auth().basic(username, password)
        .when()
                .get()
                .prettyPeek()
        .then()
                .statusCode(200);
        //@formatter:on

        assertThat(this.omniRepository.count(), is(3L));
        assertThat(this.omniRepository.countByOmniTypeAndArchived(omniType1, true), is(1L));
        assertThat(this.omniRepository.countByOmniTypeAndArchived(omniType1, false), is(1L));
        assertThat(this.omniRepository.countByOmniTypeAndArchived(omniType2, false), is(1L));
    }

    @Test
    public void deleteOmniByDataType() {
        var omni1 = TestUtils.buildOmniItem(omniType1, "Omni 2", false);
        var omni2 = TestUtils.buildOmniItem(omniType2, "Omni 3", false);
        var omni3 = TestUtils.buildOmniItem(omniType1, "Omni 4", false);

        this.omniRepository.saveAll(Arrays.asList(omni1, omni2, omni3));

        assertThat(this.omniRepository.count(), is(3L));
        assertThat(this.omniRepository.countByOmniTypeAndArchived(omniType1, false), is(2L));
        assertThat(this.omniRepository.countByOmniTypeAndArchived(omniType2, false), is(1L));

        //@formatter:off
        given()
                .baseUri(baseUrl)
                .basePath(String.format("/queue/omni/%s/purge", dataType1))
                .auth().basic(username, password)
        .when()
                .post()
                .prettyPeek()
        .then()
                .statusCode(200);
        //@formatter:on

        assertThat(this.omniRepository.count(), is(1L));
        assertThat(this.omniRepository.countByOmniTypeAndArchived(omniType1, false), is(0L));
        assertThat(this.omniRepository.countByOmniTypeAndArchived(omniType2, false), is(1L));
    }

    @Test
    public void countOmni() {
        var omni1 = TestUtils.buildOmniItem(omniType1, "Omni 2", false);
        var omni2 = TestUtils.buildOmniItem(omniType2, "Omni 3", false);
        var omni3 = TestUtils.buildOmniItem(omniType1, "Omni 4", false);

        //@formatter:off
        given()
                .baseUri(baseUrl)
                .basePath(String.format("/queue/omni/%s/count", dataType1))
                .auth().basic(username, password)
        .when()
                .get()
                .prettyPeek()
        .then()
                .statusCode(200)
                .body("dataType", is(dataType1))
                .body("itemCount", is(0));
        //@formatter:on

        this.omniRepository.saveAll(Arrays.asList(omni1, omni2, omni3));

        //@formatter:off
        given()
                .baseUri(baseUrl)
                .basePath(String.format("/queue/omni/%s/count", dataType1))
                .auth().basic(username, password)
        .when()
                .get()
                .prettyPeek()
        .then()
                .statusCode(200)
                .body("dataType", is(dataType1))
                .body("itemCount", is(2));

        given()
                .baseUri(baseUrl)
                .basePath(String.format("/queue/omni/%s/count", dataType2))
                .auth().basic(username, password)
        .when()
                .get()
                .prettyPeek()
        .then()
                .statusCode(200)
                .body("dataType", is(dataType2))
                .body("itemCount", is(1));
        //@formatter:on
    }

    @Test
    public void countOmnis() {
        var omni1 = TestUtils.buildOmniItem(omniType1, "Omni 1", false);
        var omni2 = TestUtils.buildOmniItem(omniType2, "Omni 2", false);
        var omni3 = TestUtils.buildOmniItem(omniType1, "Omni 3", false);

        //@formatter:off
        given()
                .baseUri(baseUrl)
                .basePath("/queue/omni/count")
                .auth().basic(username, password)
        .when()
                .get()
                .prettyPeek()
        .then()
                .statusCode(200)
                .body("[0].dataType", is(dataType1))
                .body("[0].itemCount", is(0))
                .body("[0].meta", is(nullValue()))
                .body("[1].dataType", is(dataType2))
                .body("[1].itemCount", is(0))
                .body("[1].meta", is(nullValue()));
        //@formatter:on

        this.omniRepository.saveAll(Arrays.asList(omni1, omni2, omni3));

        //@formatter:off
        given()
                .baseUri(baseUrl)
                .basePath("/queue/omni/count")
                .auth().basic(username, password)
        .when()
                .get()
                .prettyPeek()
        .then()
                .statusCode(200)
                .body("[0].dataType", is(dataType1))
                .body("[0].itemCount", is(2))
                .body("[0].meta", is(nullValue()))
                .body("[1].dataType", is(dataType2))
                .body("[1].itemCount", is(1))
                .body("[1].meta", is(nullValue()));
        //@formatter:on
    }

    @Test
    public void countOmnisWhenQueueIsEmpty() {
        this.omniTypeRepository.deleteAll();

        //@formatter:off
        given()
                .baseUri(baseUrl)
                .basePath("/queue/omni/count")
                .auth().basic(username, password)
        .when()
                .get()
                .prettyPeek()
        .then()
                .statusCode(200)
                .body(is("[]"));
        //@formatter:on
    }

    @Test
    public void deleteOmni() {
        var omni1 = TestUtils.buildOmniItem(omniType1, "Omni 1", false);

        var id = this.omniRepository.save(omni1).getId();

        //@formatter:off
        given()
                .baseUri(baseUrl)
                .basePath(String.format("/queue/omni/%s", id))
                .auth().basic(username, password)
        .when()
                .delete()
                .prettyPeek()
        .then()
                .statusCode(200);
        //@formatter:on

        assertThat(this.omniRepository.count(), is(0L));
    }

    @Test
    public void searchOmnis() {
        var omni1 = TestUtils.buildOmniItem(omniType1, "Omni 1", false);
        var omni2 = TestUtils.buildOmniItem(omniType2, "Omni 2", false);
        var omni3 = TestUtils.buildOmniItem(omniType1, "Omni 3", false);

        omni1.setUpdated(LocalDateTime.now());

        this.omniRepository.saveAll(Arrays.asList(omni1, omni2, omni3));

        var omniSearchDTO = new OmniSearchDTO();
        omniSearchDTO.setArchived(false);
        omniSearchDTO.setDataType(dataType1);
        omniSearchDTO.setUpdatedBeforeDate(LocalDateTime.now().plusDays(1));

        //@formatter:off
        var result =
                given()
                        .config(TestUtils.getConfig())
                        .headers(TestUtils.getHeaders())
                        .baseUri(baseUrl)
                        .basePath("/queue/omni/search")
                        .auth().basic(username, password)
                        .body(omniSearchDTO)
                .when()
                        .post()
                        .prettyPeek()
                .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .jsonPath()
                        .getList("", OmniDTO.class);
//        @formatter:on

        assertThat(result.size(), is(1));
        assertThat(result.get(0).getData(), is(omni1.getData()));
        assertThat(result.get(0).getArchived(), is(omni1.getArchived()));
        assertThat(result.get(0).getUpdated(), is(omni1.getUpdated()));
    }

    @Test
    public void searchOmnisCreatedOn() {
        var time = LocalDateTime.now().plusMinutes(1);
        var omni1 = TestUtils.buildOmniItem(omniType1, "Omni 2", false);
        var omni2 = TestUtils.buildOmniItem(omniType2, "Omni 3", false);
        var omni3 = TestUtils.buildOmniItem(omniType1, "Omni 4", true);

        this.omniRepository.saveAll(Arrays.asList(omni1, omni2, omni3));

        var omniSearchDTO = new OmniSearchDTO();
        omniSearchDTO.setArchived(true);
        omniSearchDTO.setDataType(dataType1);
        omniSearchDTO.setCreatedBeforeDate(time);

        //@formatter:off
        var result =
                given()
                        .config(TestUtils.getConfig())
                        .headers(TestUtils.getHeaders())
                        .baseUri(baseUrl)
                        .basePath("/queue/omni/search")
                        .auth().basic(username, password)
                        .body(omniSearchDTO)
                .when()
                        .post()
                        .prettyPeek()
                .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .jsonPath()
                        .getList("", OmniDTO.class);
        //@formatter:on

        assertThat(result.size(), is(1));
        assertThat(result.get(0).getData(), is(omni3.getData()));
        assertThat(result.get(0).getArchived(), is(omni3.getArchived()));
        assertThat(result.get(0).getUpdated(), is(omni3.getUpdated()));
    }

    @Test
    public void cleanOmnisTest() {
        var time1 = LocalDateTime.now().plusMinutes(1);
        var time2 = LocalDateTime.now().plusMinutes(3);

        var omni1 = TestUtils.buildOmniItem(omniType1, "Omni 1", false);

        var omni2 = TestUtils.buildOmniItem(omniType1, "Omni 2", false);
        omni2.setCreated(time2);

        this.omniRepository.saveAll(Arrays.asList(omni1, omni2));

        var archiveOmniDTO = new ArchiveOmniDTO();
        archiveOmniDTO.setDataType(dataType1);
        archiveOmniDTO.setCreatedBefore(time1);

        //@formatter:off
        given()
                .config(TestUtils.getConfig())
                .headers(TestUtils.getHeaders())
                .baseUri(baseUrl)
                .basePath("/queue/omni/clean")
                .auth().basic(username, password)
                .body(archiveOmniDTO)
        .when()
                .post()
                .prettyPeek()
        .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath();
        //@formatter:on
        var omniSearchDTO = new OmniSearchDTO();
        omniSearchDTO.setDataType(dataType1);
        omniSearchDTO.setArchived(true);
        omniSearchDTO.setCreatedBeforeDate(time1);

        //@formatter:off
        var result =
                given()
                        .config(TestUtils.getConfig())
                        .headers(TestUtils.getHeaders())
                        .baseUri(baseUrl)
                        .basePath("/queue/omni/search")
                        .auth().basic(username, password)
                        .body(omniSearchDTO)
                .when()
                        .post()
                        .prettyPeek()
                .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .jsonPath()
                        .getList("", OmniDTO.class);
        //@formatter:on
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getArchived(), is(true));
    }
}
