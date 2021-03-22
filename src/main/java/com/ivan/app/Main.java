/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package com.ivan.app;

import com.ivan.app.config.Log4jConfiguration;
import com.ivan.app.view.MainWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ivan
 */
public class Main {

    static {
        Log4jConfiguration.configure();
    }

    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(() -> {
            LOG.info("Creating MainWindow instance...");
            new MainWindow().setVisible(true);
        });
    }

}
