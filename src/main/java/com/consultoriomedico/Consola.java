package com.consultoriomedico;

import org.apache.log4j.Logger;

public class Consola {

    private static final Logger log = Logger.getLogger(Consola.class);

    public static void main(String[] args) {
        log.debug("LOG DEBUG TEST");
        log.info("LOG INFO JAVA");
    }
}