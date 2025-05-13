/*
 * This file is part of the auto1-oss/test-data-storage-service.
 *
 * (c) AUTO1 Group SE https://www.auto1-group.com
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.auto1.testdatastorage.utils;

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

import com.auto1.testdatastorage.domain.Omni;
import com.auto1.testdatastorage.domain.OmniType;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;

import java.time.LocalDateTime;
import java.util.Map;

import static io.restassured.config.EncoderConfig.encoderConfig;

@UtilityClass
public class TestUtils {

    public Omni buildOmniItem(OmniType omniType, String text, boolean archived) {
        Omni omni = new Omni();
        omni.setOmniType(omniType);
        omni.setData(text);
        omni.setArchived(archived);
        omni.setCreated(LocalDateTime.now());
        return omni;
    }

    public OmniType buildOmniTypeItem(String dataType, String meta) {
        OmniType omni = new OmniType();
        omni.setDataType(dataType);
        omni.setMeta(meta);
        omni.setCreated(LocalDateTime.now());
        return omni;
    }

    public OmniType buildOmniTypeItem(String dataType) {
        OmniType omni = new OmniType();
        omni.setDataType(dataType);
        omni.setCreated(LocalDateTime.now());
        return omni;
    }

    public RestAssuredConfig getConfig() {
        return RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs("JSON", ContentType.TEXT));
    }

    public Map<String, String> getHeaders() {
        return Map.of(HttpHeaders.CONTENT_TYPE, "application/json");
    }
}
