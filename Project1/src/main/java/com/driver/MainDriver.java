package com.driver;

import com.frontcontroller.FrontController;
import io.javalin.Javalin;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class MainDriver {
    final static Logger loggy = Logger.getLogger(MainDriver.class);
    static {
        loggy.setLevel(Level.ALL);
        //loggy.setLevel(Level.ERROR);
    }
    public static void main(String[] args) {
        Javalin app = Javalin.create(
                config-> {
                    config.addStaticFiles("/html-pages");
                    config.addStaticFiles("/javascript-files");
                    config.addStaticFiles("/css-files");
                    config.addStaticFiles("/images");
                    config.addStaticFiles("/favicon/favicon.ico");
                }
        ).start(9003);
        FrontController fc = new FrontController(app);
        loggy.info("Server has been started.");
    }
}
