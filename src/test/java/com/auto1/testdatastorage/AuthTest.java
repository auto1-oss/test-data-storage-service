/*
* This file is part of the auto1-oss/test-data-storage-service.
*
* (c) AUTO1 Group SE https://www.auto1-group.com
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/

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

import com.auto1.testdatastorage.dto.ArchiveOmniDTO;
import com.auto1.testdatastorage.dto.OmniSearchDTO;
import com.auto1.testdatastorage.dto.OmniTypeDTO;
import com.auto1.testdatastorage.utils.TestUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.function.BiFunction;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

@SpringBootTest(classes = TestDataStorageApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class AuthTest {

    @Value("http://localhost:${local.server.port}/v1")
    private String baseUrl;
    @Value("${basic.username}")
    private String validUsername;
    @Value("${basic.password}")
    private String validPassword;

    private static final String DATA_TYPE_1 = "dataType1";
    private static final Long DATA_TYPE_1_ID = 1L;

    @ParameterizedTest
    @MethodSource("provideSecuredEndpoints")
    @DisplayName("Secured endpoints must return 401 when no authorization header is provided")
    public void noAuthHeader(HttpMethod httpMethod, String path, Object body) {

        var specBase = given()
                .config(TestUtils.getConfig())
                .baseUri(baseUrl)
                .basePath(path);

        httpMethod.sendRequest.apply(specBase, body).prettyPeek().then().statusCode(401);
    }

    @ParameterizedTest
    @MethodSource("provideSecuredEndpoints")
    @DisplayName("Secured endpoints must return 401 when username is invalid")
    public void invalidUsername(HttpMethod httpMethod, String path, Object body) {
        var specBase = given()
                .config(TestUtils.getConfig())
                .auth().basic("no such user", validPassword)
                .baseUri(baseUrl)
                .basePath(path);

        httpMethod.sendRequest.apply(specBase, body).prettyPeek().then().statusCode(401);
    }

    @ParameterizedTest
    @MethodSource("provideSecuredEndpoints")
    @DisplayName("Secured endpoints must return 401 when password is invalid")
    public void invalidPassword(HttpMethod httpMethod, String path, Object body) {
        var specBase = given()
                .config(TestUtils.getConfig())
                .auth().basic(validUsername, "wrong one")
                .baseUri(baseUrl)
                .basePath(path);

        httpMethod.sendRequest.apply(specBase, body).prettyPeek().then().statusCode(401);
    }

    public static Stream<Arguments> provideSecuredEndpoints() {
        //@formatter:off
        return Stream.of(
                // Omni Queue endpoints
                Arguments.of(HttpMethod.POST, "/queue/omni/" + DATA_TYPE_1, "data"),
                Arguments.of(HttpMethod.GET, "/queue/omni/" + DATA_TYPE_1, null),
                Arguments.of(HttpMethod.POST, "/queue/omni/" + DATA_TYPE_1 + "/purge", null),
                Arguments.of(HttpMethod.GET, "/queue/omni/" + DATA_TYPE_1 + "/count", null),
                Arguments.of(HttpMethod.GET, "/queue/omni/count", null),
                Arguments.of(HttpMethod.DELETE, "/queue/omni/" + DATA_TYPE_1_ID, null),
                Arguments.of(HttpMethod.POST, "/queue/omni/search", new OmniSearchDTO()),
                Arguments.of(HttpMethod.POST, "/queue/omni/clean", new ArchiveOmniDTO()),

                // Omni Type endpoints
                Arguments.of(HttpMethod.POST, "/queue/omni-type" + DATA_TYPE_1, new OmniTypeDTO()),
                Arguments.of(HttpMethod.PUT, "/queue/omni-type/" + DATA_TYPE_1_ID, new OmniTypeDTO()),
                Arguments.of(HttpMethod.DELETE, "/queue/omni-type/" + DATA_TYPE_1_ID, null),
                Arguments.of(HttpMethod.GET, "/queue/omni-types", null)
        );
        //@formatter:on
    }

    @RequiredArgsConstructor
    public enum HttpMethod {
        POST((spec, body) -> body != null ? spec.body(body).when().post() : spec.when().post()),

        PUT((spec, body) -> body != null ? spec.body(body).when().put() : spec.when().put()),

        GET((spec, body) -> spec.when().get()),

        DELETE((spec, body) -> spec.when().delete());

        private final BiFunction<RequestSpecification, Object, Response> sendRequest;
    }
}
