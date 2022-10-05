package com.auto1.testdatastorage.utils;

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

    public Omni buildOmniItems(String dataType, String text, boolean archived) {
        Omni omni = new Omni();
        omni.setDataType(dataType);
        omni.setData(text);
        omni.setArchived(archived);
        omni.setCreated(LocalDateTime.now());
        return omni;
    }

    public OmniType buildOmniTypeItems(String dataType, String meta) {
        OmniType omni = new OmniType();
        omni.setDataType(dataType);
        omni.setMeta(meta);
        omni.setCreated(LocalDateTime.now());
        return omni;
    }

    public OmniType buildMetaEntry(String dataType, String meta) {
        OmniType entry = new OmniType();
        entry.setDataType(dataType);
        entry.setMeta(meta);
        return entry;
    }

    public RestAssuredConfig getConfig() {
        return RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs("JSON", ContentType.TEXT));
    }

    public Map<String, String> getHeaders() {
        return Map.of(HttpHeaders.CONTENT_TYPE, "application/json");
    }
}
