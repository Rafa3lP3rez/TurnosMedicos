package com.consultoriomedico.repository;

import com.consultoriomedico.domain.Doctor;
import com.consultoriomedico.domain.Especialidad;
import com.consultoriomedico.domain.Paciente;

import java.sql.SQLException;
import java.util.List;

public interface IAzureDB {

    boolean insertPacienteStatement(Paciente paciente) throws SQLException;

    boolean insertDoctorStatement(Doctor doctor) throws SQLException;

    List<Paciente> selectPaciente(boolean buscarPorIdFlag, String ... idUsuario) throws SQLException;

    List<Doctor> selectDoctor(boolean buscarPorIdFlag, String ... idDoctor) throws SQLException;

    List<Especialidad> listEspecialidades() throws SQLException;


}
