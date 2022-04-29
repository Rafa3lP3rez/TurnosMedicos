package com.consultoriomedico;

import com.consultoriomedico.service.GestionCitasImpl;
import org.apache.log4j.Logger;
import com.consultoriomedico.service.GestionUsuariosImpl;

public class Consola {

    private static final Logger log = Logger.getLogger(Consola.class);

    public static void main(String[] args) {
        log.info("Iniciando Programa");
        GestionCitasImpl gestionCitas = new GestionCitasImpl();
        gestionCitas.crearCita();
        log.info("Finalizando Programa");
    }
}