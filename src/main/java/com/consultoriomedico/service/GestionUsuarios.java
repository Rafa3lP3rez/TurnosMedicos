package com.consultoriomedico.service;


public interface GestionUsuarios {

    void crearUsuario();

    void pedirDatos();

    boolean[] validarDoctor();

    void listarUsuarios();

    void buscarUsuarioPorId();

    int seleccionarEspecialidad();
}
