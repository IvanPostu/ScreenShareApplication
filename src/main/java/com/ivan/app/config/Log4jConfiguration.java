/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package com.ivan.app.config;

import java.io.IOException;
import java.io.InputStream;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

public abstract class Log4jConfiguration {

    public static void configure() {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();

            if (loader == null) {
                loader = Class.class.getClassLoader();
            }

            InputStream inputStream = loader.getResourceAsStream("config/log4j2.dev.xml");
            ConfigurationSource source = new ConfigurationSource(inputStream);
            Configurator.initialize(null, source);
        } catch (IOException ex) {
            System.out.println(ex);
            System.exit(-1);
        }
    }

}
