package org.mule.modules.tools;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: sporcina
 * Date: 10/2/13
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestProperties {
    private static TestProperties instance = null;
    private static Properties properties;

    protected TestProperties() {
        Resource resource = new ClassPathResource("/test.properties");
        try {
            properties = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: need to add proper logging here
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
