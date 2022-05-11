package com.consultoriomedico.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import com.consultoriomedico.domain.*;
import org.apache.log4j.Logger;


public class AzureDB implements IAzureDB {
    private static final Logger log = Logger.getLogger(AzureDB.class);
    private final String connectionString;
    private static final String OUTPUT_CODE = "OUTPUT_CODE";
    private static final String MESSAGE = "MESSAGE";

    public AzureDB() {
        this.connectionString = getConnectionString();
    }

    public String getConnectionString() {
        PropertiesConfig properties = new PropertiesConfig();
        return "jdbc:sqlserver://" + properties.getPropertyConfig("SERVER_BD") + ":1433;" +
                "database=" + properties.getPropertyConfig("BD_NAME") + ";" +
                "user=" + properties.getPropertyConfig("BD_USERNAME") + "@clinicusildb;" +
                "password=" + properties.getPropertyConfig("BD_PASSWORD") + ";" +
                "encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    }

    public boolean insertPacienteStatement(Paciente paciente) throws SQLException {
        CallableStatement callableStatement = null;
        Connection cnn = null;
        boolean result = false;
        try {
            cnn = DriverManager.getConnection(connectionString);
            callableStatement = cnn.prepareCall("{CALL [dbo].[SP_CREARPACIENTE](?, ?, ?, ?, ?, ?, ?)}");
            callableStatement.setString("ID_PACIENTE", Integer.toString(paciente.getId()));
            result = insertUsuarioBaseExec(callableStatement, paciente);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cnn != null) {
                try {
                    cnn.close();
                } catch (SQLException ex) {
                    log.error(ex);
                }
            }
            if (callableStatement != null) callableStatement.close();
        }
        return result;
    }

    public boolean insertDoctorStatement(Doctor doctor) throws SQLException {
        CallableStatement callableStatement = null;
        Connection cnn = null;
        boolean result = false;
        try {
            cnn = DriverManager.getConnection(connectionString);
            callableStatement = cnn.prepareCall("{CALL [dbo].[SP_CREARDOCTOR](?, ?, ?, ?, ?, ?, ?, ?)}");
            callableStatement.setString("ID_DOCTOR", Integer.toString(doctor.getId()));
            callableStatement.setInt("ID_ESPECIALIDAD", doctor.getIdEspecialidad());
            result = insertUsuarioBaseExec(callableStatement, doctor);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cnn != null) {
                try {
                    cnn.close();
                } catch (SQLException ex) {
                    log.error(ex);
                }
            }
            if (callableStatement != null) callableStatement.close();
        }
        return result;
    }

    private boolean insertUsuarioBaseExec(CallableStatement callableBase, Usuario usuario) throws SQLException {
        int outPutCode = 0;
        String message;
        try {
            if (callableBase != null) {
                callableBase.setString("NOMBRE", usuario.getNombre());
                callableBase.setString("DIRECCION", usuario.getDireccion());
                callableBase.setString("TELEFONO", usuario.getTelefono());
                callableBase.setString("EMAIL", usuario.getEmail());
                callableBase.registerOutParameter(OUTPUT_CODE, Types.INTEGER);
                callableBase.registerOutParameter(MESSAGE, Types.NVARCHAR);
                callableBase.execute();
                outPutCode = callableBase.getInt(OUTPUT_CODE);
                message = callableBase.getNString(MESSAGE);
                log.info(String.format("OUTPUT STORE --> OUTPUT_CODE: %s | MESSAGE : %S", outPutCode, message));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (callableBase != null) callableBase.close();
        }
        return outPutCode == 1;
    }

    public List<Paciente> selectPaciente(boolean buscarPorIdFlag, String... idUsuario) throws SQLException {
        CallableStatement callableStatement = null;
        Connection cnn = null;
        ResultSet rs = null;
        int outPutCode = 0;
        String message;
        List<Paciente> listPaciente = null;
        try {
            cnn = DriverManager.getConnection(connectionString);
            callableStatement = cnn.prepareCall("{CALL [dbo].[SP_LIST_PACIENTE](?, ?, ?, ?)}");
            if ((buscarPorIdFlag)) {
                callableStatement.setString("ID_USUARIO", idUsuario[0]);
                callableStatement.setNull("FLAG_T_PACIENTE", Types.NULL);
            } else {
                callableStatement.setNull("ID_USUARIO", Types.NULL);
                callableStatement.setInt("FLAG_T_PACIENTE", 1);
            }
            callableStatement.registerOutParameter(OUTPUT_CODE, Types.INTEGER);
            callableStatement.registerOutParameter(MESSAGE, Types.NVARCHAR);
            rs = callableStatement.executeQuery();
            if (rs != null) {
                listPaciente = processResultSetPaciente(rs);
            }
            outPutCode = callableStatement.getInt(OUTPUT_CODE);
            message = callableStatement.getNString(MESSAGE);
            log.info(String.format("OUTPUT STORE --> OUTPUT_CODE: %s | MESSAGE : %S", outPutCode, message));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cnn != null) {
                try {
                    cnn.close();
                } catch (SQLException ex) {
                    log.error(ex);
                }
            }
            if (callableStatement != null) callableStatement.close();
        }
        return listPaciente;
    }

    private List<Paciente> processResultSetPaciente(ResultSet rs) throws SQLException {
        List<Paciente> listPaciente = new ArrayList<>();
        while (rs.next()) {
            listPaciente.add(
                    Paciente.builder()
                            .id(Integer.parseInt(rs.getString("ID_PACIENTE")))
                            .nombre(rs.getString("NOMBRE"))
                            .direccion(rs.getString("DIRECCION"))
                            .telefono(rs.getString("TELEFONO"))
                            .email(rs.getString("EMAIL"))
                            .build()
            );
        }
        rs.close();
        return listPaciente;
    }

    public List<Doctor> selectDoctor(boolean buscarPorIdFlag, String... idDoctor) throws SQLException {
        CallableStatement callableStatement = null;
        Connection cnn = null;
        int outPutCode = 0;
        String message;
        ResultSet rs = null;
        List<Doctor> listDoctor = null;
        try {
            cnn = DriverManager.getConnection(connectionString);
            callableStatement = cnn.prepareCall("{CALL [dbo].[SP_LIST_DOCTOR](?, ?, ?, ?)}");
            if ((buscarPorIdFlag)) {
                callableStatement.setString("ID_USUARIO", idDoctor[0]);
                callableStatement.setNull("FLAG_T_DOCTOR", Types.NULL);
            } else {
                callableStatement.setNull("ID_USUARIO", Types.NULL);
                callableStatement.setInt("FLAG_T_DOCTOR", 1);
            }
            callableStatement.registerOutParameter(OUTPUT_CODE, Types.INTEGER);
            callableStatement.registerOutParameter(MESSAGE, Types.NVARCHAR);
            rs = callableStatement.executeQuery();
            if (rs != null) {
                listDoctor = processResultSetDoctor(rs);
            }
            outPutCode = callableStatement.getInt(OUTPUT_CODE);
            message = callableStatement.getNString(MESSAGE);
            log.info(String.format("OUTPUT STORE --> OUTPUT_CODE: %s | MESSAGE : %S", outPutCode, message));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cnn != null) {
                try {
                    cnn.close();
                } catch (SQLException ex) {
                    log.error(ex);
                }
            }
            if (callableStatement != null) callableStatement.close();
        }
        return listDoctor;
    }

    private List<Doctor> processResultSetDoctor(ResultSet rs) throws SQLException {
        List<Doctor> listDoctor = new ArrayList<>();
        while (rs.next()) {
            listDoctor.add(
                    Doctor.builder()
                            .id(Integer.parseInt(rs.getString("ID_DOCTOR")))
                            .nombre(rs.getString("NOMBRE"))
                            .direccion(rs.getString("DIRECCION"))
                            .telefono(rs.getString("TELEFONO"))
                            .email(rs.getString("EMAIL"))
                            .idEspecialidad(Integer.parseInt(rs.getString("ID_ESPECIALIDAD")))
                            .build()
            );
        }
        rs.close();
        return listDoctor;
    }

    public List<Especialidad> listEspecialidades() throws SQLException {
        CallableStatement callableStatement = null;
        Connection cnn = null;
        int outPutCode = 0;
        String message;
        ResultSet rs = null;
        List<Especialidad> listEspecialidad = null;
        try {
            cnn = DriverManager.getConnection(connectionString);
            callableStatement = cnn.prepareCall("{CALL [dbo].[SP_LIST_ESPECIALIDADES](?, ?)}");
            callableStatement.registerOutParameter(OUTPUT_CODE, Types.INTEGER);
            callableStatement.registerOutParameter(MESSAGE, Types.NVARCHAR);
            rs = callableStatement.executeQuery();
            if (rs != null) {
                listEspecialidad = processResultSetEspecialidad(rs);
            }
            outPutCode = callableStatement.getInt(OUTPUT_CODE);
            message = callableStatement.getNString(MESSAGE);
            log.info(String.format("OUTPUT STORE --> OUTPUT_CODE: %s | MESSAGE : %S", outPutCode, message));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cnn != null) {
                try {
                    cnn.close();
                } catch (SQLException ex) {
                    log.error(ex);
                }
            }
            if (callableStatement != null) callableStatement.close();
        }
        return listEspecialidad;
    }

    private List<Especialidad> processResultSetEspecialidad(ResultSet rs) throws SQLException {
        List<Especialidad> listEspecialidad = new ArrayList<>();
        while (rs.next()) {
            listEspecialidad.add(
                    Especialidad.builder()
                            .idEspecialidad(Integer.parseInt(rs.getString("ID_ESPECIALIDAD")))
                            .nombreEspecialidad(rs.getString("NOMBRE"))
                            .build()
            );
        }
        rs.close();
        return listEspecialidad;
    }

    public List<Horario> listHorarioDisponiblesXRecomendacion(int idEspecialidad, String fecha) throws SQLException {
        Scanner sc = new Scanner(System.in);
        CallableStatement callableStatement = null;
        Connection cnn = null;
        int outPutCode = 0;
        String message;
        int recibioRecomendacion = 0;
        String fechaRecomendada;
        String opcionFecha = "";
        ResultSet rs = null;
        List<Horario> listEspecialidad = null;
        try {
            cnn = DriverManager.getConnection(connectionString);
            callableStatement = cnn.prepareCall("{CALL [dbo].[SP_LIST_CITA_RECOMENDACION](?, ?, ?, ?, ?, ?)}");
            callableStatement.setInt("ID_ESPECIALIDAD", idEspecialidad);
            callableStatement.setString("DIA", fecha);
            callableStatement.registerOutParameter("RECIBIO_RECOMENDACION", Types.INTEGER);
            callableStatement.registerOutParameter("FECHA_RECOMENDADA", Types.NVARCHAR);
            callableStatement.registerOutParameter(OUTPUT_CODE, Types.INTEGER);
            callableStatement.registerOutParameter(MESSAGE, Types.NVARCHAR);
            rs = callableStatement.executeQuery();
            List<Horario> listHorarioTmp = null;
            if (rs != null) {
                listHorarioTmp = processResultSetHorario(rs);
            } else {
                return Collections.emptyList();
            }
            recibioRecomendacion = callableStatement.getInt("RECIBIO_RECOMENDACION");
            fechaRecomendada = callableStatement.getNString("FECHA_RECOMENDADA");
            outPutCode = callableStatement.getInt(OUTPUT_CODE);
            message = callableStatement.getNString(MESSAGE);
            log.info(String.format("OUTPUT STORE --> OUTPUT_CODE: %s | MESSAGE : %S", outPutCode, message));
            if (recibioRecomendacion == 1) {
                System.out.printf("No se encontraron horarios disponibles para la fecha '%s', hemos encontrado horarios en la fecha '%s'. ¿Desea visualizarlos? S/N%n", fecha, fechaRecomendada);
                opcionFecha = sc.nextLine();
            }
            if (recibioRecomendacion == 0 || opcionFecha.equalsIgnoreCase("S")) {
                listEspecialidad = new ArrayList<>(listHorarioTmp);
            } else {
                System.out.println("Entendido!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cnn != null) {
                try {
                    cnn.close();
                } catch (SQLException ex) {
                    log.error(ex);
                }
            }
            if (callableStatement != null) callableStatement.close();
        }
        return listEspecialidad;
    }

    private List<Horario> processResultSetHorario(ResultSet rs) throws SQLException {
        List<Horario> listHorario = new ArrayList<>();
        while (rs.next()) {
            listHorario.add(
                    Horario.builder()
                            .idDoctor(rs.getString("ID_DOCTOR"))
                            .fecha(rs.getString("DIA"))
                            .horaInicio(rs.getString("HORA_INICIO"))
                            .horaFin(rs.getString("HORA_FIN"))
                            .build()
            );
        }
        rs.close();
        return listHorario;
    }

    public boolean insertCita(Cita cita) throws SQLException {
        CallableStatement callableStatement = null;
        Connection cnn = null;
        int outPutCode = 0;
        String message;
        try {
            cnn = DriverManager.getConnection(connectionString);
            callableStatement = cnn.prepareCall("{CALL [dbo].[SP_CREARCITA](?, ?, ?, ?, ?, ?, ?)}");
            callableStatement.setString("ID_DOCTOR", cita.getHorario().getIdDoctor());
            callableStatement.setString("ID_PACIENTE", Integer.toString(cita.getPaciente().getId()));
            callableStatement.setString("FECHA", cita.getHorario().getFecha());
            callableStatement.setString("HORA_INICIO", cita.getHorario().getHoraInicio());
            callableStatement.setString("HORA_FIN", cita.getHorario().getHoraFin());
            callableStatement.registerOutParameter(OUTPUT_CODE, Types.INTEGER);
            callableStatement.registerOutParameter(MESSAGE, Types.NVARCHAR);
            callableStatement.execute();
            outPutCode = callableStatement.getInt(OUTPUT_CODE);
            message = callableStatement.getNString(MESSAGE);
            log.info(String.format("OUTPUT STORE --> OUTPUT_CODE: %s | MESSAGE : %S", outPutCode, message));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cnn != null) {
                try {
                    cnn.close();
                } catch (SQLException ex) {
                    log.error(ex);
                }
            }
            if (callableStatement != null) callableStatement.close();
        }
        return outPutCode == 1;
    }


    public List<Cita> ListPorDoctorCita(int idDoctor) throws SQLException{
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<Cita> listCitas = new ArrayList<>();
        String query;
        try{
            connection = DriverManager.getConnection(getConnectionString());
            query = String.format("SELECT ID_CITA, ID_DOCTOR, ID_PACIENTE, FECHA, HORA_INICIO, HORA_FIN FROM T_CITA WHere ID_DOCTOR = '%s' AND ID_ESTADO= 1;", idDoctor);
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet !=null) {
                while (resultSet.next()) {
                    listCitas.add(
                            Cita.builder().id(resultSet.getInt("ID_CITA"))
                                    .paciente(selectPaciente(true, Integer.toString(resultSet.getInt("ID_PACIENTE"))).get(0))
                                    .doctor(selectDoctor(true, Integer.toString(idDoctor)).get(0))
                                    .horario(
                                            Horario.builder()
                                                    .fecha(resultSet.getString("FECHA"))
                                                    .horaInicio(resultSet.getString("HORA_INICIO"))
                                                    .horaFin(resultSet.getString("HORA_FIN"))
                                                    .build()
                                    ).build()
                    );
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    log.error(ex);
                }
            }
            if (preparedStatement != null) preparedStatement.close();
        }
        return listCitas;

    }

    public List<Cita> processResultSetListPorPacienteCita(int idPaciente) throws SQLException{
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<Cita> listCitas = new ArrayList<>();
        String query;
        try{
            connection = DriverManager.getConnection(getConnectionString());
            query = String.format("SELECT ID_CITA, ID_DOCTOR, ID_PACIENTE, FECHA, HORA_INICIO, HORA_FIN FROM T_CITA WHere ID_PACIENTE = '%s' AND ID_ESTADO= 1;", idPaciente);
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet !=null) {
                while (resultSet.next()) {
                    listCitas.add(
                            Cita.builder().id(resultSet.getInt("ID_CITA"))
                                    .paciente(selectPaciente(true, Integer.toString(idPaciente)).get(0))
                                    .doctor(selectDoctor(true, Integer.toString(resultSet.getInt("ID_DOCTOR"))).get(0))
                                    .horario(
                                            Horario.builder()
                                                    .fecha(resultSet.getString("FECHA"))
                                                    .horaInicio(resultSet.getString("HORA_INICIO"))
                                                    .horaFin(resultSet.getString("HORA_FIN"))
                                                    .build()
                                    ).build()
                    );
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    log.error(ex);
                }
            }
            if (preparedStatement != null) preparedStatement.close();
        }
        return listCitas;

    }



}
