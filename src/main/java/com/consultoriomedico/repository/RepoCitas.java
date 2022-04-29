package com.consultoriomedico.repository;


import com.consultoriomedico.domain.Cita;
import com.consultoriomedico.domain.Doctor;
import com.consultoriomedico.domain.Paciente;
import com.consultoriomedico.domain.Usuario;

public interface RepoCitas {

    void listarPorDoctor(Object[] doctores);

    void listarPorPaciente(Object[] pacientes);

    Usuario buscar(Long id);

}