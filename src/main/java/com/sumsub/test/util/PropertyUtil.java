package com.sumsub.test.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class PropertyUtil {

    public static String getProperty(String propertyValue) {
        try (
                InputStream inputStream = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("gradle.properties")).openStream();
        ) {
            Properties appProps = new Properties();
            appProps.load(inputStream);
            return appProps.getProperty(propertyValue);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
