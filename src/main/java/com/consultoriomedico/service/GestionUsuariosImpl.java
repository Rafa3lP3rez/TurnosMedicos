package com.consultoriomedico.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

import com.consultoriomedico.domain.*;
import com.consultoriomedico.repository.RepoUsuariosImpl;
import org.apache.log4j.Logger;

public class GestionUsuariosImpl implements GestionUsuarios {
    public static final Logger log = Logger.getLogger(GestionUsuariosImpl.class);

    private GestionUsuariosImpl() {

    }

    public static void crearUsuario() {
        log.info("[GestionUsuariosImpl][crearUsuario] Inicio de llamada creación usuario");
        try {
            pedirDatos();
        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    public static void pedirDatos(){
        Scanner sc = new Scanner(System.in);
        String especialidad = null;
        try {
            System.out.print("Se comenzará con la creación de usuario\nPor favor escriba el nombre del usuario: ");
            String nombreUsuario = sc.nextLine();
            System.out.print("Digite su documento de identidad: ");
            int id = sc.nextInt();
            if (RepoUsuariosImpl.builder().build().buscarPorId(id) == null) {
                sc.nextLine();
                boolean[] flagDoctorArr = validarDoctor();
                if (flagDoctorArr[1]) {
                    System.out.print("Escriba la dirección del usuario: ");
                    String direccion = sc.nextLine();
                    System.out.print("Escriba el telefono del usuario: ");
                    String telefono = sc.nextLine();
                    System.out.print("Escriba el email del usuario: ");
                    String email = sc.nextLine();

                    if (flagDoctorArr[0]) {
                        System.out.println("Escriba la especialidad del doctor: ");
                        especialidad = sc.nextLine();
                        Doctor doctor = Doctor.builder().id(id)
                                .creadoEn(new Date())
                                .especialidad(especialidad)
                                .flagDoctor(true)
                                .nombre(nombreUsuario)
                                .direccion(direccion)
                                .telefono(telefono)
                                .email(email).build();
                        log.info("[GestionUsuariosImpl][pedirDatos] -> " + doctor.toString());
                        RepoUsuariosImpl.builder().build().grabar(doctor);
                    } else {
                        Paciente paciente = Paciente.builder().id(id)
                                .creadoEn(new Date())
                                .flagDoctor(false)
                                .nombre(nombreUsuario)
                                .direccion(direccion)
                                .telefono(telefono)
                                .email(email)
                                .build();
                        RepoUsuariosImpl.builder().build().grabar(paciente);
                        log.info("[GestionUsuariosImpl][pedirDatos] -> " + paciente.toString());
                    }
                }
            } else {
                System.out.println("Ya existe un usuario con ese Documento de identidad, terminando programa");
            }
        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    public static boolean[] validarDoctor() {
        Scanner sc = new Scanner(System.in);
        int iterDoctorAnswer = 0;
        boolean flagDoctor = false;

        while (iterDoctorAnswer < 3) {
            System.out.print("El usuario es Paciente(P) o Doctor(D) elija una de las opciones (P/D): ");
            String flagDoctorString = sc.nextLine();
            if (Objects.equals(flagDoctorString, "D") || Objects.equals(flagDoctorString, "P")) {
                flagDoctor = Objects.equals(flagDoctorString, "D");
                break;
            } else {
                iterDoctorAnswer++;
                System.out.print("Opción inválida por favor digita P o D según corresponda\n");
            }
        }
        if (iterDoctorAnswer >= 3) System.out.println("Opción invalida reiterada, finalizando programa...");
        return new boolean[]{flagDoctor, iterDoctorAnswer < 3};
    }

    public static void listarUsuarios() {
        log.info("[GestionUsuariosImpl][listarUsuarios] Se listarán los usuarios y pacientes");
        ArrayList<Doctor> listaDoctores = (ArrayList<Doctor>) RepoUsuariosImpl.builder().build().listarUsuarios()[0];
        ArrayList<Paciente> listaPacientes = (ArrayList<Paciente>) RepoUsuariosImpl.builder().build().listarUsuarios()[1];
        System.out.println("Lista de doctores");
        for (Doctor doctor : listaDoctores) {
            System.out.println(doctor.toString());
        }
        System.out.println("\nLista de pacientes: ");
        for (Paciente paciente : listaPacientes) {
            System.out.println(paciente.toString());
        }
    }

    public static void buscarUsuarioPorId() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Por favor introduce el id a buscar: ");
        int id = sc.nextInt();
        log.info(String.format("[GestionUsuariosImpl][buscarUsuarioPorId] Buscando usuario por ID: %s", id));
        Usuario usuario = RepoUsuariosImpl.builder().build().buscarPorId(id);
        if (usuario != null) {
            System.out.println("Se encontró el siguiente Usuario con ese id: \n" + usuario.toString());
        } else {
            System.out.printf("No se encontró ningún usuario con el id: %s", id);
        }
    }

}
