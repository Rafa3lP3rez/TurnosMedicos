package com.consultoriomedico;

import com.consultoriomedico.service.GestionCitasImpl;
import org.apache.log4j.Logger;
import com.consultoriomedico.service.GestionUsuariosImpl;

public class Consola {

    private static final Logger log = Logger.getLogger(Consola.class);

    public static void main(String[] args) {
        log.info("Iniciando Programa");
        //GestionUsuariosImpl.builder().build().crearUsuario();
        //GestionCitasImpl.builder().build().listarCitaPorDoctor();
        //GestionCitasImpl.builder().build().crearCita();
        GestionCitasImpl.builder().build().listarCitaPorPaciente();
        log.info("Finalizando Programa");
    }
}