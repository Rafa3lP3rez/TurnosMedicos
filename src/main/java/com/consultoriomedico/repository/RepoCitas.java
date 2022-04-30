package com.consultoriomedico.repository;


import com.consultoriomedico.domain.*;

import java.util.List;

public interface RepoCitas {

    void grabar (Cita cita);

    List<Cita> listarCitasPorDoctor(Doctor doctor);

    List<Cita> listarCitasPorPaciente(Paciente paciente);

    Usuario buscar(Long id);

    void sendMailConfirmation(Usuario usuario);

}