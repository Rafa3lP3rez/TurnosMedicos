package com.consultoriomedico.service;

import com.consultoriomedico.domain.Cita;
import com.consultoriomedico.domain.Doctor;
import com.consultoriomedico.domain.Usuario;
import com.consultoriomedico.repository.RepoCitasImpl;
import com.consultoriomedico.repository.RepoUsuariosImpl;
import lombok.Builder;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Builder
public class GestionCitasImpl implements GestionCitas {

    public static final Logger log = Logger.getLogger(GestionCitasImpl.class);
    private static final DateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

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
        try {
            System.out.print("Se comenzará con la creación de la cita\nPor favor escriba en que especialidad quiere su cita: ");
            String especialidadCita = sc.nextLine();

            List<Doctor> especialidadesPorDoctor = RepoUsuariosImpl.builder().build().listarDoctoresPorEspeciliadad(especialidadCita);

            for (Doctor doctor : especialidadesPorDoctor) {
                System.out.println("ID Doctor = " + doctor.getId() + ", nombre del doctor = " + doctor.getNombre() + ", especialidad = " + doctor.getEspecialidad());
            }

            System.out.print("Por favor ingrese el ID del doctor según la especialidad que necesita:\n ");
            int idDoctor = sc.nextInt();
            sc.nextLine();

            System.out.print("Por favor ingrese el ID del paciente que necesita:\n ");
            int idPa = sc.nextInt();
            sc.nextLine();

            Usuario idPaciente = RepoUsuariosImpl.builder().build().buscarPorId(idPa);

            if (idPaciente != null && !idPaciente.isFlagDoctor()){
                System.out.println("Por favor ingrese la fecha de la cita en el formato: yyyy-mm-dd");
                String fechaCita = sc.nextLine();
                Date date = dt1.parse(fechaCita);

                Cita cita = Cita.builder().idCita(1)
                        .idDoctor(idDoctor)
                        .idPaciente(idPaciente.getId())
                        .fecha(date)
                        .creadoEn(new Date())
                        .build();
                RepoCitasImpl.builder().build().grabar(cita);
                log.info("[GestionCitasImpl][pedirDatos] -> " + cita.toString());
            } else{
                System.out.println("No se pudo registrar la cita, el paciente no esta registrado");
                GestionUsuariosImpl.builder().build().crearUsuario();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarCitaPorDoctor(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Por favor digita el id del doctor: ");
        int idDoctor = sc.nextInt();
        Doctor doctor = RepoUsuariosImpl.builder().build().buscarDoctorPorId(idDoctor);
        if (doctor != null ){
            System.out.printf("Para el doctor %s con id %s se tienen las siguientes citas: %n%n", doctor.getNombre(), doctor.getId());
            ArrayList<Cita> listaCitas = (ArrayList<Cita>) RepoCitasImpl.builder().build().listarCitasPorDoctor(doctor);
            if (!listaCitas.isEmpty()){
                for (Cita cita: listaCitas) {
                    System.out.println(cita);
                }
            } else System.out.println("No se encontraron citas asociadas con ese doctor");
        } else  {
            System.out.printf("No se encuentra ningún doctor con el id: %s", idDoctor);
        }
    }




}
