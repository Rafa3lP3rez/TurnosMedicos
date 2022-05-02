package com.consultoriomedico.repository;


import com.consultoriomedico.domain.*;

import java.util.List;

public interface RepoCitas {

    void grabar(Cita cita, Usuario usuario, Doctor doctor);

    List<Cita> listarCitasPorDoctor(Doctor doctor);

    List<Cita> listarCitasPorPaciente(Paciente paciente);

    int obtenerIdCita();

    Usuario buscar(Long id);

    void sendMailConfirmation(Usuario usuario, Cita cita, Doctor doctor);

    Cita buscarPorId(int id);

}