package com.khye;

import java.io.InputStream;

import com.khye.config.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class App {
    private Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        App app = new App();
        JDAConnector jdaConnector = new JDAConnector(app.setUpConfig());
        jdaConnector.connect();
    }

    private Configuration setUpConfig() {
        Yaml yaml = new Yaml(new Constructor(Configuration.class));
        InputStream file = Configuration.class.getClassLoader().getResourceAsStream("application-props.yml");
        return yaml.load(file); }
}
