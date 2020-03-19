package com.xavidop.alexa.properties;

import java.io.IOException;
import java.util.Properties;

public final class PropertiesUtils {

    private PropertiesUtils() {

    }

    public static String getPropertyValue(String Key) {
        Properties prop = new Properties();

        try {
            prop.load(PropertiesUtils.class.getResourceAsStream("/application.properties"));
            return prop.getProperty(Key);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}
