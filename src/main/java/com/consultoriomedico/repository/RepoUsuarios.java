package com.consultoriomedico.repository;

import com.consultoriomedico.domain.Doctor;
import com.consultoriomedico.domain.Especialidad;
import com.consultoriomedico.domain.Paciente;

import java.util.List;

public interface RepoUsuarios {

    void grabarPaciente(Paciente paciente);

    void grabarDoctor(Doctor doctor);

    Doctor buscarDoctorPorId(int id);

    Paciente buscarPacientePorId(int id);

    List<Doctor> listarDoctores();

    List<Paciente> listarPacientes();

    List<Especialidad> listarEspecialidades();

}
