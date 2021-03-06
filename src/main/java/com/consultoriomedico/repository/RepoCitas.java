package com.consultoriomedico.repository;


import com.consultoriomedico.domain.*;

import java.util.List;

public interface RepoCitas {

    void grabarCita(Cita cita);

    List<Cita> listarCitasPorDoctor(int idPaciente);

    List<Cita> listarCitasPorPaciente(int idPaciente);

    Cita buscarPorId(int id);

    List<Horario> listarHorariosDisponibles(int idEspecialidad, String fecha);
}