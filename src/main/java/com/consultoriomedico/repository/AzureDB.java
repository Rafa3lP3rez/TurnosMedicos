package com.consultoriomedico.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.consultoriomedico.domain.*;
import org.apache.log4j.Logger;


public class AzureDB implements IAzureDB{
    private static final Logger log = Logger.getLogger(AzureDB.class);
    private final String connectionString;
    private static final String OUTPUT_CODE = "OUTPUT_CODE";
    private static final String MESSAGE = "MESSAGE";

    public AzureDB() {
        this.connectionString = getConnectionString();
    }

    private String getConnectionString() {
        PropertiesConfig properties = new PropertiesConfig();
        return "jdbc:sqlserver://" + properties.getPropertyConfig("SERVER_BD") + ":1433;" +
                "database=" + properties.getPropertyConfig("BD_NAME") + ";" +
                "user=" + properties.getPropertyConfig("BD_USERNAME") + "@clinicusildb;" +
                "password=" + properties.getPropertyConfig("BD_PASSWORD") + ";" +
                "encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    }

    public boolean insertPacienteStatement(Paciente paciente) throws SQLException {
        CallableStatement cstmnt = null;
        Connection cnn = null;
        boolean result = false;
        try {
            cnn = DriverManager.getConnection(connectionString);
            cstmnt = cnn.prepareCall("{CALL [dbo].[SP_CREARPACIENTE](?, ?, ?, ?, ?, ?, ?)}");
            cstmnt.setString("ID_PACIENTE", Integer.toString(paciente.getId()));
            result = insertUsuarioBaseExec(cstmnt, paciente);
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
            if (cstmnt != null) cstmnt.close();
        }
        return result;
    }

    public boolean insertDoctorStatement(Doctor doctor) throws SQLException {
        CallableStatement cstmnt = null;
        Connection cnn = null;
        boolean result = false;
        try {
            cnn = DriverManager.getConnection(connectionString);
            cstmnt = cnn.prepareCall("{CALL [dbo].[SP_CREARDOCTOR](?, ?, ?, ?, ?, ?, ?, ?)}");
            cstmnt.setString("ID_DOCTOR", Integer.toString(doctor.getId()));
            cstmnt.setInt("ID_ESPECIALIDAD", doctor.getIdEspecialidad());
            result = insertUsuarioBaseExec(cstmnt, doctor);
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
            if (cstmnt != null) cstmnt.close();
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
                System.out.println(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (callableBase != null) callableBase.close();
        }
        return outPutCode == 1;
    }

    public List<Paciente> selectPaciente(boolean buscarPorIdFlag, String ... idUsuario) throws SQLException {
        CallableStatement cstmnt = null;
        Connection cnn = null;
        ResultSet rs = null;
        int outPutCode = 0;
        String message;
        List<Paciente> listPaciente = null;
        try {
            cnn = DriverManager.getConnection(connectionString);
            cstmnt = cnn.prepareCall("{CALL [dbo].[SP_LIST_PACIENTE](?, ?, ?, ?)}");
            if ((buscarPorIdFlag)) {
                cstmnt.setString("ID_USUARIO", idUsuario[0]);
                cstmnt.setNull("FLAG_T_PACIENTE", Types.NULL);
            } else {
                cstmnt.setNull("ID_USUARIO", Types.NULL);
                cstmnt.setInt("FLAG_T_PACIENTE", 1);
            }
            cstmnt.registerOutParameter(OUTPUT_CODE, Types.INTEGER);
            cstmnt.registerOutParameter(MESSAGE, Types.NVARCHAR);
            rs = cstmnt.executeQuery();
            if (rs != null){
                listPaciente = processResultSetPaciente(rs);
            }
            outPutCode = cstmnt.getInt(OUTPUT_CODE);
            message = cstmnt.getNString(MESSAGE);
            log.info(String.format("OUTPUT STORE --> OUTPUT_CODE: %s | MESSAGE : %S", outPutCode, message));
            System.out.println(message);
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
            if (cstmnt != null) cstmnt.close();
        }
        return listPaciente;
    }

    private List<Paciente> processResultSetPaciente (ResultSet rs) throws SQLException {
        List<Paciente> listPaciente = new ArrayList<>();
        while(rs.next()){
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

    public List<Doctor> selectDoctor(boolean buscarPorIdFlag, String ... idDoctor) throws SQLException{
        CallableStatement cstmnt = null;
        Connection cnn = null;
        int outPutCode = 0;
        String message;
        ResultSet rs = null;
        List<Doctor> listDoctor = null;
        try {
            cnn = DriverManager.getConnection(connectionString);
            cstmnt = cnn.prepareCall("{CALL [dbo].[SP_LIST_DOCTOR](?, ?, ?, ?)}");
            if ((buscarPorIdFlag)) {
                cstmnt.setString("ID_USUARIO", idDoctor[0]);
                cstmnt.setNull("FLAG_T_DOCTOR", Types.NULL);
            } else {
                cstmnt.setNull("ID_USUARIO", Types.NULL);
                cstmnt.setInt("FLAG_T_DOCTOR", 1);
            }
            cstmnt.registerOutParameter(OUTPUT_CODE, Types.INTEGER);
            cstmnt.registerOutParameter(MESSAGE, Types.NVARCHAR);
            rs = cstmnt.executeQuery();
            if (rs != null){
                listDoctor = processResultSetDoctor(rs);
            }
            outPutCode = cstmnt.getInt(OUTPUT_CODE);
            message = cstmnt.getNString(MESSAGE);
            log.info(String.format("OUTPUT STORE --> OUTPUT_CODE: %s | MESSAGE : %S", outPutCode, message));
            System.out.println(message);
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
            if (cstmnt != null) cstmnt.close();
        }
        return listDoctor;
    }

    private List<Doctor> processResultSetDoctor(ResultSet rs) throws SQLException {
        List<Doctor> listDoctor = new ArrayList<>();
        while(rs.next()){
            listDoctor.add(
                    Doctor.builder()
                            .id(Integer.parseInt(rs.getString("ID_DOCTOR")))
                            .nombre(rs.getString("NOMBRE"))
                            .direccion(rs.getString("DIRECCION"))
                            .telefono(rs.getString("TELEFONO"))
                            .email(rs.getString("EMAIL"))
                            .build()
            );
        }
        rs.close();
        return listDoctor;
    }

    public  List<Especialidad> listEspecialidades() throws SQLException {
        CallableStatement cstmnt = null;
        Connection cnn = null;
        int outPutCode = 0;
        String message;
        ResultSet rs = null;
        List<Especialidad> listEspecialidad = null;
        try {
            cnn = DriverManager.getConnection(connectionString);
            cstmnt = cnn.prepareCall("{CALL [dbo].[SP_LIST_ESPECIALIDADES](?, ?)}");
            cstmnt.registerOutParameter(OUTPUT_CODE, Types.INTEGER);
            cstmnt.registerOutParameter(MESSAGE, Types.NVARCHAR);
            rs = cstmnt.executeQuery();
            if (rs != null) {
                listEspecialidad = processResultSetEspecialidad(rs);
            }
            outPutCode = cstmnt.getInt(OUTPUT_CODE);
            message = cstmnt.getNString(MESSAGE);
            log.info(String.format("OUTPUT STORE --> OUTPUT_CODE: %s | MESSAGE : %S", outPutCode, message));
            System.out.println(message);
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cnn != null) {
                try {
                    cnn.close();
                } catch (SQLException ex) {
                    log.error(ex);
                }
            }
            if (cstmnt != null) cstmnt.close();
        }
        return listEspecialidad;
    }

    private List<Especialidad> processResultSetEspecialidad(ResultSet rs) throws SQLException{
        List<Especialidad> listEspecialidad = new ArrayList<>();
        while(rs.next()){
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

}
