package com.consultoriomedico.repository;


import com.consultoriomedico.domain.*;

import java.util.List;

public interface RepoCitas {

    void grabarCita(Cita cita);

    List<Cita> listarCitasPorDoctor(Doctor doctor);

    List<Cita> listarCitasPorPaciente(Paciente paciente);

    Cita buscarPorId(int id);

    List<Horario> listarHorariosDisponibles(int idEspecialidad, String fecha);
}