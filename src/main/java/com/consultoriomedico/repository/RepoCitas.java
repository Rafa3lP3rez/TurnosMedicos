package com.consultoriomedico.repository;


import com.consultoriomedico.domain.*;

import java.util.List;

public interface RepoCitas {

    void grabar (Cita cita);

    List<Cita> listarCitasPorDoctor(Doctor doctor);

    void listarPorPaciente(Object[] pacientes);

    Usuario buscar(Long id);

    void sendMailConfirmation(Usuario usuario);

}