package com.auto1.testdatastorage;


import com.auto1.testdatastorage.domain.Omni;
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
import java.util.List;

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

    @BeforeEach
    public void setup() {
        this.omniRepository.deleteAll();
        this.omniTypeRepository.deleteAll();
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

        Omni omni = this.omniRepository.findFirstByDataTypeAndArchivedOrderByIdAsc(dataType, false);
        assertThat(omni.getData(), is(json));
    }

    @Test
    public void getOmni() {
        String dataType1 = "test1";
        String dataType2 = "test2";
        Omni omni1 = Omni.builder().dataType(dataType1).data("Omni 2").archived(false).build();
        Omni omni2 = Omni.builder().dataType(dataType2).data("Omni 3").archived(false).build();
        Omni omni3 = Omni.builder().dataType(dataType1).data("Omni 4").archived(false).build();

        this.omniRepository.saveAll(Arrays.asList(omni1, omni2, omni3));

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
        assertThat(this.omniRepository.countByDataTypeAndArchived(dataType1, true), is(1L));
        assertThat(this.omniRepository.countByDataTypeAndArchived(dataType1, false), is(1L));
        assertThat(this.omniRepository.countByDataTypeAndArchived(dataType2, false), is(1L));
    }

    @Test
    public void deleteOmniByDataType() {
        String dataType1 = "test1";
        String dataType2 = "test2";
        Omni omni1 = TestUtils.buildOmniItems(dataType1, "Omni 2", false);
        Omni omni2 = TestUtils.buildOmniItems(dataType2, "Omni 3", false);
        Omni omni3 = TestUtils.buildOmniItems(dataType1, "Omni 4", false);

        this.omniRepository.saveAll(Arrays.asList(omni1, omni2, omni3));

        assertThat(this.omniRepository.count(), is(3L));
        assertThat(this.omniRepository.countByDataTypeAndArchived(dataType1, false), is(2L));
        assertThat(this.omniRepository.countByDataTypeAndArchived(dataType2, false), is(1L));

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
        assertThat(this.omniRepository.countByDataTypeAndArchived(dataType1, false), is(0L));
        assertThat(this.omniRepository.countByDataTypeAndArchived(dataType2, false), is(1L));
    }

    @Test
    public void purgeOmni() {
        String dataType1 = "test1";
        String dataType2 = "test2";
        Omni omni1 = TestUtils.buildOmniItems(dataType1, "Omni 2", false);
        Omni omni2 = TestUtils.buildOmniItems(dataType2, "Omni 3", false);
        Omni omni3 = TestUtils.buildOmniItems(dataType1, "Omni 4", false);

        this.omniRepository.saveAll(Arrays.asList(omni1, omni2, omni3));

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
        Omni omni1 = TestUtils.buildOmniItems(dataType1, "Omni 2", false);
        Omni omni2 = TestUtils.buildOmniItems(dataType2, "Omni 3", false);
        Omni omni3 = TestUtils.buildOmniItems(dataType1, "Omni 4", false);

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

        this.omniRepository.saveAll(Arrays.asList(omni1, omni2, omni3));

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
    public void countOmnis() {
        String dataType1 = "test1";
        String meta = "meta data";
        String dataType2 = "test2";
        Omni omni1 = TestUtils.buildOmniItems(dataType1, "Omni 2", false);
        Omni omni2 = TestUtils.buildOmniItems(dataType2, "Omni 3", false);
        Omni omni3 = TestUtils.buildOmniItems(dataType1, "Omni 4", false);

        OmniType entry1 = TestUtils.buildMetaEntry(dataType1, meta);
        OmniType entry2 = TestUtils.buildMetaEntry(dataType2, null);

        omniTypeRepository.saveAll(Arrays.asList(entry1, entry2));

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

        omniRepository.saveAll(Arrays.asList(omni1, omni2, omni3));

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
                .body("[0].meta", is(meta))
                .body("[1].dataType", is(dataType2))
                .body("[1].itemCount", is(1))
                .body("[1].meta", is(nullValue()));
        //@formatter:on
    }

    @Test
    public void deleteOmni() {
        String dataType1 = "test1";
        Omni omni1 = TestUtils.buildOmniItems(dataType1, "Omni 1", false);

        long id = this.omniRepository.save(omni1).getId();

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
        Omni omni1 = TestUtils.buildOmniItems(dataType1, "Omni 2", false);
        Omni omni2 = TestUtils.buildOmniItems(dataType2, "Omni 3", false);
        Omni omni3 = TestUtils.buildOmniItems(dataType1, "Omni 4", false);

        omni1.setUpdated(LocalDateTime.now());

        this.omniRepository.saveAll(Arrays.asList(omni1, omni2, omni3));

        OmniSearchDTO omniSearchDTO = new OmniSearchDTO();
        omniSearchDTO.setArchived(false);
        omniSearchDTO.setDataType(dataType1);
        omniSearchDTO.setUpdatedBeforeDate(LocalDateTime.now().plusDays(1));

        //@formatter:off
        List<OmniDTO> result =
                given()
                        .config(TestUtils.getConfig())
                        .headers(TestUtils.getHeaders())
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
        assertThat(result.get(0).getDataType(), is(omni1.getDataType()));
        assertThat(result.get(0).getData(), is(omni1.getData()));
        assertThat(result.get(0).getArchived(), is(omni1.getArchived()));
        assertThat(result.get(0).getUpdated(), is(omni1.getUpdated()));
    }

    @Test
    public void searchOmnisCreatedOn() {
        String dataType1 = "test1";
        String dataType2 = "test2";
        LocalDateTime time = LocalDateTime.now().plusMinutes(1);
        Omni omni1 = TestUtils.buildOmniItems(dataType1, "Omni 2", false);
        Omni omni2 = TestUtils.buildOmniItems(dataType2, "Omni 3", false);
        Omni omni3 = TestUtils.buildOmniItems(dataType1, "Omni 4", true);

        this.omniRepository.saveAll(Arrays.asList(omni1, omni2, omni3));

        OmniSearchDTO omniSearchDTO = new OmniSearchDTO();
        omniSearchDTO.setArchived(true);
        omniSearchDTO.setDataType(dataType1);
        omniSearchDTO.setCreatedBeforeDate(time);

        //@formatter:off
        List<OmniDTO> result =
                given()
                        .config(TestUtils.getConfig())
                        .headers(TestUtils.getHeaders())
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
        assertThat(result.get(0).getDataType(), is(omni3.getDataType()));
        assertThat(result.get(0).getData(), is(omni3.getData()));
        assertThat(result.get(0).getArchived(), is(omni3.getArchived()));
        assertThat(result.get(0).getUpdated(), is(omni3.getUpdated()));
    }

    @Test
    public void cleanOmnisTest() {
        String dataType1 = "CleanOmni1";
        LocalDateTime time1 = LocalDateTime.now().plusMinutes(1);
        LocalDateTime time2 = LocalDateTime.now().plusMinutes(3);

        Omni omni1 = TestUtils.buildOmniItems(dataType1, "Omni 1", false);

        Omni omni2 = TestUtils.buildOmniItems(dataType1, "Omni 2", false);
        omni2.setCreated(time2);

        this.omniRepository.saveAll(Arrays.asList(omni1, omni2));

        ArchiveOmniDTO archiveOmniDTO = new ArchiveOmniDTO();
        archiveOmniDTO.setDataType(dataType1);
        archiveOmniDTO.setCreatedBefore(time1);

        //@formatter:off
        given()
                .config(TestUtils.getConfig())
                .headers(TestUtils.getHeaders())
                .baseUri(baseUrl)
                .basePath("/queue/omni/clean")
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
        OmniSearchDTO omniSearchDTO = new OmniSearchDTO();
        omniSearchDTO.setDataType(dataType1);
        omniSearchDTO.setArchived(true);
        omniSearchDTO.setCreatedBeforeDate(time1);

        //@formatter:off
        List<OmniDTO> result =
                given()
                        .config(TestUtils.getConfig())
                        .headers(TestUtils.getHeaders())
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
        assertThat(result.get(0).getArchived(), is(true));
    }
}
