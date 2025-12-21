package com.github.maximovj.rhhub_app.config.env;

import org.slf4j.Logger;
import java.util.Map;

public class PrintMessages {

    public static Logger logger;

    public static void show(String modo, Map<String, Object> messages) {
       logger.info("* * * * * * * * * * * * * * * * * *");
        logger.info("*");
        logger.info("*");
        logger.info("*");
        logger.info("* MODO = [ "+modo+" ]");
        if(messages != null ){
            messages.forEach((clave, valor) -> {
                System.out.println("\t\t | " + clave + " = [ " + valor + " ] ");
                System.out.println("\t\t *");
            });
        }
        logger.info("*");
        logger.info("*");
        logger.info("*");
        logger.info("* * * * * * * * * * * * * * * * * *"); 
    }

}
