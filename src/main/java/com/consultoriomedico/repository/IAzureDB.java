package com.consultoriomedico.repository;

import com.consultoriomedico.domain.*;

import java.sql.SQLException;
import java.util.List;

public interface IAzureDB {

    boolean insertPacienteStatement(Paciente paciente) throws SQLException;

    boolean insertDoctorStatement(Doctor doctor) throws SQLException;

    List<Paciente> selectPaciente(boolean buscarPorIdFlag, String ... idUsuario) throws SQLException;

    List<Doctor> selectDoctor(boolean buscarPorIdFlag, String ... idDoctor) throws SQLException;

    List<Especialidad> listEspecialidades() throws SQLException;

    List<Horario> listHorarioDisponiblesXRecomendacion(int idEspecialidad, String fecha) throws SQLException;

    boolean insertCita(Cita cita) throws SQLException;


}
