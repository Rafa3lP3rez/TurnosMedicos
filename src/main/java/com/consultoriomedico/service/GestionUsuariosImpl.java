package com.consultoriomedico.service;

import java.util.Date;
import java.util.Objects;
import java.util.Scanner;
import com.consultoriomedico.domain.*;
import com.consultoriomedico.repository.RepoUsuariosImpl;
import org.apache.log4j.Logger;

public class GestionUsuariosImpl implements GestionUsuarios{
    public static final Logger log = Logger.getLogger(GestionUsuariosImpl.class);

    private GestionUsuariosImpl(){

    }

    public static void crearUsuario(){
        Scanner sc = new Scanner(System.in);
        log.info("[GestionUsuariosImpl][crearUsuario] Inicio de llamada creación usuario");
        String especialidad = null;
        try {
            System.out.print("Se comenzará con la creación de usuario\nPor favor escriba el nombre del usuario: ");
            String nombreUsuario = sc.nextLine();
            System.out.print("Digite su documento de identidad: ");
            int id = sc.nextInt();
            sc.nextLine();
            int iterDoctorAnswer = 0;
            boolean flagDoctor = false;
            while (iterDoctorAnswer < 3) {
                System.out.print("El usuario es Paciente(P) o Doctor(D) elija una de las opciones (P/D): ");
                String flagDoctorString = sc.nextLine();
                if (Objects.equals(flagDoctorString, "D") || Objects.equals(flagDoctorString, "P")) {
                    flagDoctor = Objects.equals(flagDoctorString, "D");
                    break;
                } else {
                    iterDoctorAnswer ++;
                    System.out.print("Opción inválida por favor digita P o D según corresponda\n");
                }
            }
            if (iterDoctorAnswer < 3) {
                if (flagDoctor) {
                    System.out.println("Escriba la especialidad del doctor: ");
                    especialidad = sc.nextLine();
                }
                System.out.print("Escriba la dirección del usuario: ");
                String direccion = sc.nextLine();
                System.out.print("Escriba el telefono del usuario: ");
                String telefono = sc.nextLine();
                System.out.print("Escriba el email del usuario: ");
                String email = sc.nextLine();

                if (flagDoctor) {
                    Doctor doctor = Doctor.builder().id(id)
                            .creadoEn(new Date())
                            .especialidad(especialidad)
                            .flagDoctor(true)
                            .nombre(nombreUsuario)
                            .direccion(direccion)
                            .telefono(telefono)
                            .email(email).build();
                    log.info("[GestionUsuariosImpl][crearUsuario] -> " + doctor.toString());
                    RepoUsuariosImpl.grabar(doctor);
                } else {
                    Paciente paciente = Paciente.builder().id(id)
                            .creadoEn(new Date())
                            .flagDoctor(false)
                            .nombre(nombreUsuario)
                            .direccion(direccion)
                            .telefono(telefono)
                            .email(email)
                            .build();
                    RepoUsuariosImpl.grabar(paciente);
                    log.info("[GestionUsuariosImpl][crearUsuario] -> " + paciente.toString());
                }
            }
        }
        catch (Exception ex) {
            log.error(ex.toString());
        }

    }

}
