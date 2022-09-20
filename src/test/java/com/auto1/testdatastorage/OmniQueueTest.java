package com.auto1.testdatastorage;


import com.auto1.testdatastorage.domain.OmniQueueItem;
import com.auto1.testdatastorage.repository.OmniRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDataStorageApplication.class)
public class OmniQueueTest {

    @Autowired
    private OmniRepository omniRepository;

    @Value("http://localhost:${local.server.port}/v1")
    private String baseUrl;

    @BeforeEach
    public void setup() {
        this.omniRepository.deleteAll();
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
                .basePath(String.format("queue/omni/%s", dataType1))
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
}
