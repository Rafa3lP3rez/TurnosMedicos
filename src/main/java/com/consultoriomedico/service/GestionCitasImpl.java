package com.consultoriomedico.service;

import com.consultoriomedico.domain.*;

import com.consultoriomedico.repository.RepoCitas;
import com.consultoriomedico.repository.RepoCitasImpl;
import com.consultoriomedico.repository.RepoUsuarios;
import com.consultoriomedico.repository.RepoUsuariosImpl;
import lombok.Builder;
import org.apache.log4j.Logger;

import javax.print.Doc;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Builder
public class GestionCitasImpl implements GestionCitas {

    public static final Logger log = Logger.getLogger(GestionCitasImpl.class);
    private static final DateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    public void crearCita() {
        log.info("[GestionUsuariosImpl][crearCita] Inicio de llamada creación cita");
        try {
            obtenerDatos();
        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    public void obtenerDatos() {
        Scanner sc = new Scanner(System.in);
        RepoUsuarios repoUsuarios = RepoUsuariosImpl.builder().build();
        RepoCitas repoCitas = RepoCitasImpl.builder().build();
        GestionUsuarios gestionUsuarios = GestionUsuariosImpl.builder().build();
        int idEspecialidad;
        try {
            System.out.print("Se comenzará con la creación de la cita\n ");
            idEspecialidad = gestionUsuarios.seleccionarEspecialidad();
            System.out.print("Por favor ingrese el ID del paciente que necesita:\n ");
            int idPa = sc.nextInt();
            sc.nextLine();
            Paciente paciente = repoUsuarios.buscarPacientePorId(idPa);
            if (paciente != null) {
                System.out.printf("Por favor %s ingrese la fecha de la cita en el formato: 'yyyy-mm-dd' | EJ : '2022-06-26'%n", paciente.getNombre());
                String fechaCita = sc.nextLine();
                List<Horario> listHorarios = repoCitas.listarHorariosDisponibles(idEspecialidad, fechaCita);
                System.out.println("Lista de horarios: \n");
                System.out.printf("%15s %15s %15s %15s %n", "ID", "DIA", "HORA_INICIO", "HORA_FIN");
                int index = 0;
                for (Horario horario : listHorarios) {
                    System.out.printf("%15s %15s %15s %15s %n", index + 1,
                            horario.getFecha(), horario.getHoraInicio().substring(0, 8), horario.getHoraFin().substring(0, 8));
                    index++;
                }
                System.out.println("Seleccione el id del horario que desea reservar");
                int opcId = sc.nextInt() - 1;
                if (opcId < listHorarios.size()) {
                    Horario horario = listHorarios.get(opcId);
                    Doctor doctor = repoUsuarios.buscarDoctorPorId(Integer.parseInt(horario.getIdDoctor()));
                    repoCitas.grabarCita(
                            Cita.builder()
                                    .horario(horario)
                                    .paciente(paciente)
                                    .doctor(doctor)
                                    .build()
                    );
                } else {
                    System.out.println("Opción invalida");
                }

            } else {
                System.out.println("No se pudo registrar la cita, el paciente no esta registrado");
                gestionUsuarios.crearUsuario();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buscarCitaPorID() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Por favor, digita el id a buscar:");
        int id = sc.nextInt();
        log.info(String.format("[GestionCitasImpl][buscarCitaPorId] Buscando cita por ID %s", id));
        Cita cita = RepoCitasImpl.builder().build().buscarPorId(id);
        if( cita != null) {
            System.out.println("Se encontró la siguiente cita con ese id: \n" + cita);
        } else {
            System.out.printf("No se encontró ningua cita con el id: %s", id);
        }
    }

    public void listarCitaPorDoctor() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Por favor digita el id del doctor: ");
        int idDoctor = sc.nextInt();
        RepoUsuarios repoUsuario = RepoUsuariosImpl.builder().build();
        RepoCitas repoCitas = RepoCitasImpl.builder().build();
        Doctor doctor = repoUsuario.buscarDoctorPorId(idDoctor);
        if (doctor != null) {
            System.out.printf("Para el doctor %s con id %s se tienen las siguientes citas: %n%n", doctor.getNombre(), doctor.getId());
            ArrayList<Cita> listaCitas = (ArrayList<Cita>) RepoCitasImpl.builder().build().listarCitasPorDoctor(idDoctor);
            if (!listaCitas.isEmpty()) {
                for (Cita cita : listaCitas) {
                    System.out.printf("%15s %15s %15s %15s", cita.getId(), cita.getDoctor().toString(), cita.getPaciente().toString(), cita.getHorario().toString());
                }
            } else System.out.println("No se encontraron citas asociadas con ese doctor");
        } else {
            System.out.printf("No se encuentra ningún doctor con el id: %s", idDoctor);
        }

    }

    public void listarCitaPorPaciente() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Por favor digita el id del paciente: ");
        int idPaciente = sc.nextInt();
        RepoUsuarios repoUsuario = RepoUsuariosImpl.builder().build();
        RepoCitas repoCitas = RepoCitasImpl.builder().build();
        Paciente paciente = repoUsuario.buscarPacientePorId(idPaciente);
        if (paciente != null) {
            List<Cita> listCitas = repoCitas.listarCitasPorPaciente(idPaciente);
            if (listCitas != null) {
                System.out.printf("Para el paciente %s con id %s se tienen las siguientes citas: %n%n", paciente.getNombre(), paciente.getId());
                if (!listCitas.isEmpty()) {
                    for (Cita cita : listCitas) {
                        System.out.printf("%15s %15s %15s %15s", cita.getId(), cita.getDoctor().toString(), cita.getPaciente().toString(), cita.getHorario().toString());
                    }
                } else System.out.println("No se encontraron citas asociadas con ese paciente");
            } else {
                System.out.printf("No se encuentra paciente con el id: %s", idPaciente);
            }
        } else {
            System.out.println("No existe un paciente con ese ID");
        }
    }
}
