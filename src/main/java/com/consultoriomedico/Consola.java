package com.consultoriomedico;

import com.consultoriomedico.service.GestionCitasImpl;
import org.apache.log4j.Logger;
import com.consultoriomedico.service.GestionUsuariosImpl;

import java.util.Scanner;

public class Consola {


    private static final Logger log = Logger.getLogger(Consola.class);

    //region menu y submenus
    static int subMenuUsuario() {
        int op;
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        sb.append("Bienvenido al Menu usuarios").append(System.lineSeparator());
        sb.append("1. Crear usuario").append(System.lineSeparator());
        sb.append("2. Listar usuarios").append(System.lineSeparator());
        sb.append("3. Buscar usuario por ID").append(System.lineSeparator());
        sb.append("4. Volver a menu principal").append(System.lineSeparator());
        sb.append("Seleccione la opción:");
        System.out.println(sb);
        op = sc.nextInt();
        return op;
    }

    static int subMenuCita() {
        int op;
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        sb.append("Bienvenido al Menu citas").append(System.lineSeparator());
        sb.append("1. Crear cita").append(System.lineSeparator());
        sb.append("2. Listar citas por paciente").append(System.lineSeparator());
        sb.append("3. Listar citas por doctor").append(System.lineSeparator());
        sb.append("4. Buscar cita por ID").append(System.lineSeparator());
        sb.append("5. Volver a menu principal").append(System.lineSeparator());
        sb.append("Seleccione la opción:");
        System.out.println(sb);
        op = sc.nextInt();
        return op;
    }

    static int menu() {
        int op;
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        sb.append("Bienvenido al Menu clinica Kodigo").append(System.lineSeparator());
        sb.append("1. Gestion de usuarios").append(System.lineSeparator());
        sb.append("2. Gestion citas").append(System.lineSeparator());
        sb.append("3. Salir").append(System.lineSeparator());
        sb.append("Seleccione la opción:");
        System.out.println(sb);
        op = sc.nextInt();
        return op;
    }

    //endregion
    public static void main(String[] args) {
        log.info("Iniciando Programa");
        int opc;
        GestionUsuariosImpl gestionUsuarios = GestionUsuariosImpl.builder().build();
        GestionCitasImpl gestionCitas =  GestionCitasImpl.builder().build();
        do {
            opc = menu();
            switch (opc) {
                case 1 -> {
                    //region SubMenu usuarios
                    int opUsuario = subMenuUsuario();
                    do {
                        switch (opUsuario) {
                            case 1 -> {
                                gestionUsuarios.crearUsuario();
                                System.out.println("Usuario creado exitosamente");
                            }
                            case 2 -> gestionUsuarios.listarUsuarios();
                            case 3 -> gestionUsuarios.buscarUsuarioPorId();
                            case 4 -> System.out.println("Volviendo al menu principal");
                            default -> System.out.println("La opcion no esta disponible");
                        }
                        opUsuario = subMenuUsuario();
                    } while (opUsuario != 4);
                }
                //endregion
                case 2 -> {
                    //region SubMenu citas
                    int opCita = subMenuCita();
                    do {
                        switch (opCita) {
                            case 1 -> gestionCitas.crearCita();
                            case 2 -> gestionCitas.listarCitaPorPaciente();
                            case 3 -> gestionCitas.listarCitaPorDoctor();
                            case 4 -> gestionCitas.buscarCitaPorID();
                            default -> System.out.println("La opcion no esta disponible");
                        }
                        opCita = subMenuCita();
                    } while (opCita != 5);
                }
                //endregion
                case 3 -> System.out.println("¡Hasta pronto!");
                default -> System.out.println("La opcion no está disponible");
            }
        } while (opc != 3);

        log.info("Finalizando Programa");
    }
}