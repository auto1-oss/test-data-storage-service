package com.auto1.testdatastorage.utils;

import lombok.experimental.UtilityClass;

import java.util.Base64;

@UtilityClass
public class BasicAuthUtil {

    public String getBasicAuthHeader(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }
}
