package com.consultoriomedico.service;

import com.consultoriomedico.domain.Cita;
import com.consultoriomedico.domain.Doctor;
import com.consultoriomedico.domain.Paciente;
import com.consultoriomedico.domain.Usuario;
import com.consultoriomedico.repository.RepoCitasImpl;
import com.consultoriomedico.repository.RepoUsuariosImpl;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class GestionCitasImpl implements GestionCitas {

    public static final Logger log = Logger.getLogger(GestionCitasImpl.class);
    private static final DateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    @Override
    public void crearCita() {
        log.info("[GestionUsuariosImpl][crearUsuario] Inicio de llamada creación usuario");
        try {
            obtenerDatos();
        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    public void obtenerDatos() {
        Scanner sc = new Scanner(System.in);
        String especialidad;
        try {
            System.out.print("Se comenzará con la creación de la cita\nPor favor escriba en que especialidad quiere su cita: ");
            String especialidadCita = sc.nextLine();

            List<Doctor> especialidadesPorDoctor = RepoUsuariosImpl.builder().build().listarDoctoresPorEspeciliadad(especialidadCita);

            for (Doctor doctor : especialidadesPorDoctor) {
                System.out.println("ID Doctor = " + doctor.getId() + ", nombre del doctor = " + doctor.getNombre() + ", especialidad = " + doctor.getEspecialidad());
            }

            System.out.print("Por favor ingrese el ID del doctor según la especialidad que necesita:\n ");
            int id_doctor = sc.nextInt();

            System.out.print("Por favor ingrese el ID del paciente que necesita:\n ");
            int idPa = sc.nextInt();

            Usuario idPaciente = RepoUsuariosImpl.builder().build().buscarPorId(idPa);

            if (!idPaciente.isFlagDoctor()){
                System.out.println("Por favor ingrese la fecha de la cita en el formato: yyyy-mm-dd");
                String fechaCita = sc.nextLine();
                Date date = dt1.parse(fechaCita);

                Cita cita = Cita.builder().id_cita(1)
                        .id_doctor(id_doctor)
                        .id_paciente(idPaciente.getId())
                        .fecha(date)
                        .creadoEn(new Date())
                        .build();
                RepoCitasImpl.builder().build().grabar(cita);
                log.info("[GestionCitasImpl][pedirDatos] -> " + cita.toString());
            } else{
                System.out.println("No se pudo registrar la cita");
            }




        } catch (Exception e) {
            e.printStackTrace();
        }
    }


   /* public int pedirDatosPaciente() {
        Scanner sc = new Scanner(System.in);
        String especialidad;
        int id;
        try {
            id = sc.nextInt();
            if (RepoUsuariosImpl.builder().build().buscarPorId(id) == null) {
                sc.nextLine();
                System.out.print("Se comenzará con la creación de usuario\nPor favor escriba el nombre del usuario: ");
                String nombreUsuario = sc.nextLine();
                System.out.print("Escriba la dirección del usuario: ");
                String direccion = sc.nextLine();
                System.out.print("Escriba el telefono del usuario con la extensión de su país: ");
                String telefono = sc.nextLine();
                System.out.print("Escriba el email del usuario: ");
                String email = sc.nextLine();


                Paciente paciente = Paciente.builder().id(id)
                        .creadoEn(new Date())
                        .flagDoctor(false)
                        .nombre(nombreUsuario)
                        .direccion(direccion)
                        .telefono(telefono)
                        .email(email)
                        .build();
                RepoUsuariosImpl.builder().build().grabar(paciente);
                log.info("[GestionCitasImpl][pedirDatos] -> " + paciente.toString());
                return id;
            } else {
                System.out.println("Ya existe un paciente con ese Documento de identidad asi que se asignara el id");
                Usuario usuarioId = RepoUsuariosImpl.builder().build().buscarPorId(id);

                id = usuarioId.getId();
                return id;
            }
        } catch (Exception ex) {
            log.error(ex.toString());
        }

        return 0;
    }*/




}
