package com.consultoriomedico;

import com.consultoriomedico.service.GestionCitasImpl;
import org.apache.log4j.Logger;
import com.consultoriomedico.service.GestionUsuariosImpl;

import java.util.Scanner;

public class Consola {


    private static final Logger log = Logger.getLogger(Consola.class);
    //region menu y submenus
    static int subMenuUsuario(){
        int op;
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        sb.append("Bienvenido al Menu usuarios").append(System.lineSeparator());
        sb.append("1. Crear usuario").append(System.lineSeparator());
        sb.append("2. Listar usuarios").append(System.lineSeparator());
        sb.append("3. Buscar usuario por ID").append(System.lineSeparator());
        sb.append("5. Volver a menu principal").append(System.lineSeparator());
        sb.append("Seleccione la opción:");
        System.out.println(sb);
        op = sc.nextInt();
        return op;
    }
    static int subMenuCita(){
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
    static int menu(){
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
        do {
            opc = menu();
            switch (opc){
                case 1:
                    //region SubMenu usuarios
                    int opUsuario = subMenuUsuario();
                    do{
                        switch (opUsuario){
                            case 1:
                                GestionUsuariosImpl.builder().build().crearUsuario();
                                System.out.println("Usuario creado exitosamente");
                                break;
                            case 2:
                                GestionUsuariosImpl.builder().build().listarUsuarios();
                                break;
                            case 3:
                                GestionUsuariosImpl.builder().build().buscarUsuarioPorId();
                                break;
                            case 4:
                                System.out.println("Volviendo al menu principal");
                                break;
                            default:
                                System.out.println("La opcion no esta disponible");
                        }
                        //System.out.print("\033[H\033[2J");
                        opUsuario = subMenuUsuario();
                    }while (opUsuario != 4);
                    break;
                    //endregion
                case 2:
                    //region SubMenu citas
                    //System.out.print("\033[H\033[2J");
                    int opCita = subMenuCita();
                    do{
                        switch (opCita){
                            case 1:
                                GestionCitasImpl.builder().build().crearCita();
                                break;
                            case 2:
                                GestionCitasImpl.builder().build().listarCitaPorPaciente();
                                break;
                            case 3:
                                GestionCitasImpl.builder().build().listarCitaPorDoctor();
                                break;
                            case 4:
                                //Buscar cita por id
                                break;
                            case 5:
                                //Salir
                                break;
                            default:
                                System.out.println("La opcion no esta disponible");
                        }
                        opCita = subMenuCita();
                    }while (opCita != 5);
                    break;
                    //endregion
                case 3:
                    System.out.println("¡Hasta pronto!");
                    break;
                default:
                    System.out.println("La opcion no está disponible");
            }
        } while (opc != 3);


        //GestionUsuariosImpl.builder().build().crearUsuario();
        //GestionCitasImpl.builder().build().listarCitaPorDoctor();
        //GestionCitasImpl.builder().build().crearCita();
        //GestionCitasImpl.builder().build().listarCitaPorPaciente();
        log.info("Finalizando Programa");
    }
}