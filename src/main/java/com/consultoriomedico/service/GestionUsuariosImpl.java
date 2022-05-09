package com.consultoriomedico.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

import com.consultoriomedico.domain.*;
import com.consultoriomedico.repository.RepoUsuarios;
import com.consultoriomedico.repository.RepoUsuariosImpl;
import lombok.Builder;
import org.apache.log4j.Logger;


@Builder
public class GestionUsuariosImpl implements GestionUsuarios {
    public static final Logger log = Logger.getLogger(GestionUsuariosImpl.class);
    RepoUsuarios repoUsuarios;

    public void crearUsuario() {
        log.info("[GestionUsuariosImpl][crearUsuario] Inicio de llamada creación usuario");
        try {
            pedirDatos();
        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    public void pedirDatos() {
        Scanner sc = new Scanner(System.in);
        repoUsuarios = RepoUsuariosImpl.builder().build();
        int idEspecialidad;
        try {
            System.out.print("Se comenzará con la creación de usuario\nPor favor escriba el nombre del usuario: ");
            String nombreUsuario = sc.nextLine();
            System.out.print("Digite su documento de identidad: ");
            int id = sc.nextInt();
            sc.nextLine();
            boolean[] flagDoctorArr = validarDoctor();
            if (flagDoctorArr[1]) {
                System.out.print("Escriba la dirección del usuario: ");
                String direccion = sc.nextLine();
                System.out.print("Escriba el telefono del usuario con la extensión de su país: ");
                String telefono = sc.nextLine();
                System.out.print("Escriba el email del usuario: ");
                String email = sc.nextLine();

                if (flagDoctorArr[0]) {
                    Doctor doctor = Doctor.builder().id(id)
                            .creadoEn(new Date())
                            .nombre(nombreUsuario)
                            .direccion(direccion)
                            .telefono(telefono)
                            .email(email).build();
                    crearDoctor(doctor);

                } else {
                    Paciente paciente = Paciente.builder().id(id)
                            .creadoEn(new Date())
                            .nombre(nombreUsuario)
                            .direccion(direccion)
                            .telefono(telefono)
                            .email(email)
                            .build();
                    repoUsuarios.grabarPaciente(paciente);
                    log.info("[GestionUsuariosImpl][pedirDatos] -> " + paciente.toString());
                }
            }
        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    private void crearDoctor(Doctor doctor){
        Scanner sc = new Scanner(System.in);
        int idEspecialidad;
        repoUsuarios = RepoUsuariosImpl.builder().build();
        try {
            ArrayList<Especialidad> listEspecialidad = (ArrayList<Especialidad>) repoUsuarios.listarEspecialidades();
            System.out.printf("%15s %15s %n", "Especialidad", "Nombre");
            for (Especialidad especialidad : listEspecialidad ) {
                System.out.printf("%15s %15s %n", especialidad.getIdEspecialidad(), especialidad.getNombreEspecialidad());
            }
            System.out.println("Digite el id de la especialidad asociada al doctor: ");
            idEspecialidad = sc.nextInt();
            doctor.setIdEspecialidad(idEspecialidad);
            log.info("[GestionUsuariosImpl][pedirDatos] -> " + doctor.toString());
            repoUsuarios.grabarDoctor(doctor);
        }catch (Exception e){
            log.error(e);
        }
    }

    public boolean[] validarDoctor() {
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

    public void listarUsuarios() {
        log.info("[GestionUsuariosImpl][listarUsuarios] Se listarán los usuarios y pacientes");
        repoUsuarios = RepoUsuariosImpl.builder().build();
        ArrayList<Doctor> listaDoctores = (ArrayList<Doctor>) repoUsuarios.listarDoctores();
        ArrayList<Paciente> listaPacientes = (ArrayList<Paciente>) repoUsuarios.listarPacientes();
        System.out.println("Lista de doctores: \n");
        System.out.printf("%15s %15s %15s %25s %15s %15s %n", "ID_DOCTOR", "ID_ESPECIALIDAD", "NOMBRE", "DIRECCION", "TELEFONO", "EMAIL");
        for (Doctor doctor : listaDoctores) {
            System.out.printf("%15s %15s %15s %25s %15s %15s %n", doctor.getId(),
                    doctor.getIdEspecialidad(), doctor.getNombre(), doctor.getDireccion(), doctor.getTelefono(), doctor.getEmail());
        }
        System.out.println("\nLista de pacientes: \n");
        System.out.printf("%15s %15s %25s %15s %15s %n", "ID_PACIENTE",  "NOMBRE", "DIRECCION", "TELEFONO", "EMAIL");
        for (Paciente paciente : listaPacientes) {
            System.out.printf("%15s %15s %25s %15s %15s %n", paciente.getId(), paciente.getNombre(),
                    paciente.getDireccion(), paciente.getTelefono(), paciente.getEmail());
        }
    }

    public void buscarUsuarioPorId() {
        repoUsuarios = RepoUsuariosImpl.builder().build();
        Scanner sc = new Scanner(System.in);
        System.out.println("Desea buscar un paciente o un doctor (P/D): ");
        String opcBuscar = sc.nextLine();
        System.out.println("Por favor introduce el id a buscar: ");
        int id = sc.nextInt();
        log.info(String.format("[GestionUsuariosImpl][buscarUsuarioPorId] Buscando usuario por ID: %s", id));
        if (opcBuscar.equalsIgnoreCase("P")){
            Paciente paciente = repoUsuarios.buscarPacientePorId(id);
            if (paciente != null) System.out.printf("Se encontró el siguiente paciente %s%n", paciente);
        } else if (opcBuscar.equalsIgnoreCase("D")) {
            Doctor doctor = repoUsuarios.buscarDoctorPorId(id);
            if (doctor != null) System.out.printf("Se encontró el siguiente paciente %s%n", doctor);
        } else {
            System.out.println("Opción inválida \n");
        }
    }


}
