package com.consultoriomedico.service;

import java.util.Date;
import java.util.Objects;
import java.util.Scanner;
import com.consultoriomedico.domain.*;
import com.consultoriomedico.repository.RepoUsuariosImpl;
import org.apache.log4j.Logger;

public class GestionUsuariosImpl implements GestionUsuarios{
    public static final Logger log = Logger.getLogger(GestionUsuariosImpl.class);

    private GestionUsuariosImpl() {

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
            System.out.print("El usuario es Paciente(P) o Doctor(D) elija una de las opciones (P/D): ");
            String flagDoctorString = sc.nextLine();
            boolean flagDoctor = Objects.equals(flagDoctorString, "D");
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
                Doctor doctor = new Doctor(id, new Date(), true, nombreUsuario, direccion, telefono, email, especialidad );
                RepoUsuariosImpl.grabar(doctor);
            } else {
                Paciente paciente = new Paciente(id, new Date(), false, nombreUsuario, direccion, telefono, email);
                RepoUsuariosImpl.grabar(paciente);
            }

        } catch (Exception ex) {
            log.error(ex.toString());
        }

    }
}
