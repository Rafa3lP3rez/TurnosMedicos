package com.consultoriomedico.domain;


import lombok.Data;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Data
public class PropertiesConfig {
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";
    private static final Logger log = Logger.getLogger(PropertiesConfig.class);
    private Properties propertiesConfigVar = null;

    public PropertiesConfig() {
        cargarProperties();
    }

    private void cargarProperties() {
        Properties propConfig = new Properties();
        try (FileInputStream propInput = new FileInputStream(CONFIG_FILE_PATH)) {
            propConfig.load(propInput);
        } catch (IOException e) {
            log.error(e);
        }
        propertiesConfigVar = propConfig;
    }

    public String getPropertyConfig(String propertyName) {
        if (propertiesConfigVar != null) return propertiesConfigVar.getProperty(propertyName);
        else return "";
    }
}
