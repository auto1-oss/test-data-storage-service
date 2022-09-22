package com.auto1.testdatastorage;


import com.auto1.testdatastorage.domain.OmniQueueItem;
import com.auto1.testdatastorage.domain.TypeOwner;
import com.auto1.testdatastorage.dto.CleanOmniDTO;
import com.auto1.testdatastorage.dto.OmniDTO;
import com.auto1.testdatastorage.dto.OmniSearchDTO;
import com.auto1.testdatastorage.repository.OmniRepository;
import com.auto1.testdatastorage.repository.TypeOwnersRepository;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDataStorageApplication.class)
public class OmniQueueTest {

    @Autowired
    private OmniRepository omniRepository;
    @Autowired
    private TypeOwnersRepository typeOwnersRepository;

    @Value("http://localhost:${local.server.port}/v1")
    private String baseUrl;

    @BeforeEach
    public void setup() {
        this.omniRepository.deleteAll();
        this.typeOwnersRepository.deleteAll();
    }

    @Test
    public void createOmni() {
        String dataType = "test";
        String json = "{\"omni\":\"test value\"}";

        //@formatter:off
        given()
                .baseUri(baseUrl)
                .basePath(String.format("/queue/omni/%s", dataType))
                .body(json)
        .when()
                .post()
                .prettyPeek()
        .then()
                .statusCode(201);
        //@formatter:on

        assertThat(this.omniRepository.count(), is(1L));

        OmniQueueItem omniQueueItem = this.omniRepository.findFirstByDataTypeAndDirtyOrderByIdAsc(dataType, false);
        assertThat(omniQueueItem.getData(), is(json));
    }

    @Test
    public void getOmni() {
        String dataType1 = "test1";
        String dataType2 = "test2";
        OmniQueueItem omniQueueItem1 = OmniQueueItem.builder().dataType(dataType1).data("Omni 2").dirty(false).build();
        OmniQueueItem omniQueueItem2 = OmniQueueItem.builder().dataType(dataType2).data("Omni 3").dirty(false).build();
        OmniQueueItem omniQueueItem3 = OmniQueueItem.builder().dataType(dataType1).data("Omni 4").dirty(false).build();

        this.omniRepository.saveAll(Arrays.asList(omniQueueItem1, omniQueueItem2, omniQueueItem3));

        //@formatter:off
        given()
                .baseUri(baseUrl)
                .basePath(String.format("/queue/omni/%s", dataType1))
        .when()
                .get()
                .prettyPeek()
        .then()
                .statusCode(200);
        //@formatter:on

        assertThat(this.omniRepository.count(), is(3L));
        assertThat(this.omniRepository.countByDataTypeAndDirty(dataType1, true), is(1L));
        assertThat(this.omniRepository.countByDataTypeAndDirty(dataType1, false), is(1L));
        assertThat(this.omniRepository.countByDataTypeAndDirty(dataType2, false), is(1L));
    }

    @Test
    public void deleteOmniByDataType() {
        String dataType1 = "test1";
        String dataType2 = "test2";
        OmniQueueItem omniQueueItem1 = buildOmniQueueItem(dataType1, "Omni 2", false);
        OmniQueueItem omniQueueItem2 = buildOmniQueueItem(dataType2, "Omni 3", false);
        OmniQueueItem omniQueueItem3 = buildOmniQueueItem(dataType1, "Omni 4", false);

        this.omniRepository.saveAll(Arrays.asList(omniQueueItem1, omniQueueItem2, omniQueueItem3));

        assertThat(this.omniRepository.count(), is(3L));
        assertThat(this.omniRepository.countByDataTypeAndDirty(dataType1, false), is(2L));
        assertThat(this.omniRepository.countByDataTypeAndDirty(dataType2, false), is(1L));

        //@formatter:off
        given()
                .baseUri(baseUrl)
                .basePath(String.format("/queue/omni/%s/purge", dataType1))
        .when()
                .post()
                .prettyPeek()
        .then()
                .statusCode(200);
        //@formatter:on

        assertThat(this.omniRepository.count(), is(1L));
        assertThat(this.omniRepository.countByDataTypeAndDirty(dataType1, false), is(0L));
        assertThat(this.omniRepository.countByDataTypeAndDirty(dataType2, false), is(1L));
    }

    @Test
    public void purgeOmni() {
        String dataType1 = "test1";
        String dataType2 = "test2";
        OmniQueueItem omniQueueItem1 = buildOmniQueueItem(dataType1, "Omni 2", false);
        OmniQueueItem omniQueueItem2 = buildOmniQueueItem(dataType2, "Omni 3", false);
        OmniQueueItem omniQueueItem3 = buildOmniQueueItem(dataType1, "Omni 4", false);

        this.omniRepository.saveAll(Arrays.asList(omniQueueItem1, omniQueueItem2, omniQueueItem3));

        //@formatter:off
        given()
                .baseUri(baseUrl)
                .basePath("/queue/omni/purge")
        .when()
                .post()
                .prettyPeek()
        .then()
                .statusCode(200);
        //@formatter:on

        assertThat(this.omniRepository.count(), is(0L));
    }

    @Test
    public void countOmni() {
        String dataType1 = "test1";
        String dataType2 = "test2";
        OmniQueueItem omniQueueItem1 = buildOmniQueueItem(dataType1, "Omni 2", false);
        OmniQueueItem omniQueueItem2 = buildOmniQueueItem(dataType2, "Omni 3", false);
        OmniQueueItem omniQueueItem3 = buildOmniQueueItem(dataType1, "Omni 4", false);

        //@formatter:off
        given()
                .baseUri(baseUrl)
                .basePath(String.format("/queue/omni/%s/count", dataType1))
        .when()
                .get()
                .prettyPeek()
        .then()
                .statusCode(200)
                .body("dataType", is(dataType1))
                .body("itemCount", is(0));
        //@formatter:on

        this.omniRepository.saveAll(Arrays.asList(omniQueueItem1, omniQueueItem2, omniQueueItem3));

        //@formatter:off
        given()
                .baseUri(baseUrl)
                .basePath(String.format("/queue/omni/%s/count", dataType1))
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
    public void countOmniTypes() {
        String dataType1 = "test1";
        String owner1 = "data type owner 1";
        String dataType2 = "test2";
        OmniQueueItem omniQueueItem1 = buildOmniQueueItem(dataType1, "Omni 2", false);
        OmniQueueItem omniQueueItem2 = buildOmniQueueItem(dataType2, "Omni 3", false);
        OmniQueueItem omniQueueItem3 = buildOmniQueueItem(dataType1, "Omni 4", false);

        TypeOwner entry1 = buildTypeOwnerEntry(dataType1, owner1);
        TypeOwner entry2 = buildTypeOwnerEntry(dataType2, null);

        typeOwnersRepository.saveAll(Arrays.asList(entry1, entry2));

        //@formatter:off
        given()
                .baseUri(baseUrl)
                .basePath("/queue/omni/count")
                .when()
                .get()
                .prettyPeek()
                .then()
                .statusCode(200)
                .body(is("[]"));
        //@formatter:on

        omniRepository.saveAll(Arrays.asList(omniQueueItem1, omniQueueItem2, omniQueueItem3));

        //@formatter:off
        given()
                .baseUri(baseUrl)
                .basePath("/queue/omni/count")
                .when()
                .get()
                .prettyPeek()
                .then()
                .statusCode(200)
                .body("[0].dataType", is(dataType1))
                .body("[0].itemCount", is(2))
                .body("[0].owner", is(owner1))
                .body("[1].dataType", is(dataType2))
                .body("[1].itemCount", is(1))
                .body("[1].owner", is(nullValue()));
        //@formatter:on
    }

    @Test
    public void deleteOmni() {
        String dataType1 = "test1";
        OmniQueueItem omniQueueItem1 = buildOmniQueueItem(dataType1, "Omni 1", false);

        long id = this.omniRepository.save(omniQueueItem1).getId();

        //@formatter:off
        given()
                .baseUri(baseUrl)
                .basePath(String.format("/queue/omni/%s", id))
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
        String dataType1 = "test1";
        String dataType2 = "test2";
        OmniQueueItem omniQueueItem1 = buildOmniQueueItem(dataType1, "Omni 2", false);
        OmniQueueItem omniQueueItem2 = buildOmniQueueItem(dataType2, "Omni 3", false);
        OmniQueueItem omniQueueItem3 = buildOmniQueueItem(dataType1, "Omni 4", false);

        omniQueueItem1.setUpdated(LocalDateTime.now());

        this.omniRepository.saveAll(Arrays.asList(omniQueueItem1, omniQueueItem2, omniQueueItem3));

        OmniSearchDTO omniSearchDTO = new OmniSearchDTO();
        omniSearchDTO.setDirty(false);
        omniSearchDTO.setDataType(dataType1);
        omniSearchDTO.setUpdatedBeforeDate(LocalDateTime.now().plusDays(1));

        //@formatter:off
        List<OmniDTO> result =
                given()
                        .config(getConfig())
                        .headers(getHeaders())
                        .baseUri(baseUrl)
                        .basePath("/queue/omni/search/")
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
        assertThat(result.get(0).getDataType(), is(omniQueueItem1.getDataType()));
        assertThat(result.get(0).getData(), is(omniQueueItem1.getData()));
        assertThat(result.get(0).getDirty(), is(omniQueueItem1.getDirty()));
        assertThat(result.get(0).getUpdated(), is(omniQueueItem1.getUpdated()));
    }

    @Test
    public void searchOmnisCreatedOn() {
        String dataType1 = "test1";
        String dataType2 = "test2";
        LocalDateTime time = LocalDateTime.now().plusMinutes(1);
        OmniQueueItem omniQueueItem1 = buildOmniQueueItem(dataType1, "Omni 2", false);
        OmniQueueItem omniQueueItem2 = buildOmniQueueItem(dataType2, "Omni 3", false);
        OmniQueueItem omniQueueItem3 = buildOmniQueueItem(dataType1, "Omni 4", true);

        this.omniRepository.saveAll(Arrays.asList(omniQueueItem1, omniQueueItem2, omniQueueItem3));

        OmniSearchDTO omniSearchDTO = new OmniSearchDTO();
        omniSearchDTO.setDirty(true);
        omniSearchDTO.setDataType(dataType1);
        omniSearchDTO.setCreatedBeforeDate(time);

        //@formatter:off
        List<OmniDTO> result =
                given()
                        .config(getConfig())
                        .headers(getHeaders())
                        .baseUri(baseUrl)
                        .basePath("/queue/omni/search")
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
        assertThat(result.get(0).getDataType(), is(omniQueueItem3.getDataType()));
        assertThat(result.get(0).getData(), is(omniQueueItem3.getData()));
        assertThat(result.get(0).getDirty(), is(omniQueueItem3.getDirty()));
        assertThat(result.get(0).getUpdated(), is(omniQueueItem3.getUpdated()));
    }

    @Test
    public void cleanOmnisTest() {
        String dataType1 = "CleanOmni1";
        LocalDateTime time1 = LocalDateTime.now().plusMinutes(1);
        LocalDateTime time2 = LocalDateTime.now().plusMinutes(3);

        OmniQueueItem omniQueueItem1 = buildOmniQueueItem(dataType1, "Omni 1", false);

        OmniQueueItem omniQueueItem2 = buildOmniQueueItem(dataType1, "Omni 2", false);
        omniQueueItem2.setCreated(time2);

        this.omniRepository.saveAll(Arrays.asList(omniQueueItem1, omniQueueItem2));

        CleanOmniDTO cleanOmniDTO = new CleanOmniDTO();
        cleanOmniDTO.setDataType(dataType1);
        cleanOmniDTO.setCreatedBefore(time1);

        //@formatter:off
        given()
                .config(getConfig())
                .headers(getHeaders())
                .baseUri(baseUrl)
                .basePath("/queue/omni/clean")
                .body(cleanOmniDTO)
        .when()
                .post()
                .prettyPeek()
        .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath();
        //@formatter:on
        OmniSearchDTO omniSearchDTO = new OmniSearchDTO();
        omniSearchDTO.setDataType(dataType1);
        omniSearchDTO.setDirty(true);
        omniSearchDTO.setCreatedBeforeDate(time1);

        //@formatter:off
        List<OmniDTO> result =
                given()
                        .config(getConfig())
                        .headers(getHeaders())
                        .baseUri(baseUrl)
                        .basePath("/queue/omni/search")
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
        assertThat(result.get(0).getDirty(), is(true));
    }

    private OmniQueueItem buildOmniQueueItem(String dataType, String text, boolean dirty) {
        OmniQueueItem omniQueueItem = new OmniQueueItem();
        omniQueueItem.setDataType(dataType);
        omniQueueItem.setData(text);
        omniQueueItem.setDirty(dirty);
        omniQueueItem.setCreated(LocalDateTime.now());
        return omniQueueItem;
    }

    private TypeOwner buildTypeOwnerEntry(String dataType, String owner) {
        TypeOwner entry = new TypeOwner();
        entry.setDataType(dataType);
        entry.setOwner(owner);
        return entry;
    }

    private RestAssuredConfig getConfig() {
        return RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs("JSON", ContentType.TEXT));
    }

    private Map<String, String> getHeaders() {
        return Map.of(HttpHeaders.CONTENT_TYPE, "application/json");
    }
}
