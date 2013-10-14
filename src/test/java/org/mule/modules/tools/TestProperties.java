package org.mule.modules.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * // TODO: need a description - sporcina (Oct.13,2013)
 */
public class TestProperties {

    private static final Logger logger = LoggerFactory.getLogger(TestProperties.class);

    private static TestProperties instance = null;
    private static Properties properties;

    protected TestProperties() {
        Resource resource = new ClassPathResource("/test.properties");
        try {
            properties = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            logger.error("Unable to load test.properties file");
        }
    }

    public static TestProperties getInstance() {
        if (instance == null) {
            instance = new TestProperties();
        }
        return instance;
    }

    public String get(String name) {
        return properties.getProperty(name);
    }
}
