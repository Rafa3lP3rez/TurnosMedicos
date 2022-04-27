package com.consultoriomedico;

import org.apache.log4j.Logger;
import com.consultoriomedico.service.GestionUsuariosImpl;

public class Consola {

    private static final Logger log = Logger.getLogger(Consola.class);

    public static void main(String[] args) {
        log.debug("Iniciando Programa");
        GestionUsuariosImpl.crearUsuario();
        log.debug("Finalizando Programa");
    }
}