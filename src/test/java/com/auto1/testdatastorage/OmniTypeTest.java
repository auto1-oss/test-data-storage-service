package com.auto1.testdatastorage;


import com.auto1.testdatastorage.domain.OmniType;
import com.auto1.testdatastorage.dto.OmniTypeDTO;
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
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(classes = TestDataStorageApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class OmniTypeTest {

    @Autowired
    private OmniTypeRepository omniTypeRepository;

    @Value("http://localhost:${local.server.port}/v1")
    private String baseUrl;

    @BeforeEach
    public void setup() {
        this.omniTypeRepository.deleteAll();
    }

    @Test
    public void createOmniType() {
        OmniType omniType = OmniType.builder()
                .dataType("Test data")
                .meta("Test meta")
                .created(LocalDateTime.now())
                .build();

        //@formatter:off
        given()
                .config(TestUtils.getConfig())
                .headers(TestUtils.getHeaders())
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
        OmniTypeDTO result =
                given()
                        .config(TestUtils.getConfig())
                        .headers(TestUtils.getHeaders())
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
        String dataType1 = "data type 1";
        String dataType2 = "data type 2";
        OmniType omniType1 = TestUtils.buildOmniTypeItems(dataType1, "omni type 1");
        OmniType omniType2 = TestUtils.buildOmniTypeItems(dataType2, "omni type 2");

        this.omniTypeRepository.saveAll(Arrays.asList(omniType1, omniType2));

        assertThat(this.omniTypeRepository.count(), is(2L));

        var omniTypeId = this.omniTypeRepository.findByDataType(dataType1).get().getId();

        //@formatter:off
        given()
                .baseUri(baseUrl)
                .basePath(String.format("/queue/omni-type/%s", omniTypeId))
                .when()
                .delete()
                .prettyPeek()
                .then()
                .statusCode(200);
        //@formatter:on

        assertThat(this.omniTypeRepository.count(), is(1L));
        assertThat(this.omniTypeRepository.countByDataType(dataType1), is(0L));
    }

    @Test
    public void getAllOmniTypes() {
        String dataType1 = "data type 1";
        String dataType2 = "data type 2";
        OmniType omniType1 = TestUtils.buildOmniTypeItems(dataType1, "omni type 1");
        OmniType omniType2 = TestUtils.buildOmniTypeItems(dataType2, "omni type 2");

        this.omniTypeRepository.saveAll(Arrays.asList(omniType1, omniType2));

        assertThat(this.omniTypeRepository.count(), is(2L));

        //@formatter:off
        List<OmniTypeDTO> result =
                given()
                        .baseUri(baseUrl)
                        .basePath("/queue/omni-types")
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

}
