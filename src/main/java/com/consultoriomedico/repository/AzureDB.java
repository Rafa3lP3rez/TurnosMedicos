package com.consultoriomedico.repository;

import java.sql.*;

import com.consultoriomedico.domain.Doctor;
import com.consultoriomedico.domain.PropertiesConfig;
import com.consultoriomedico.domain.Paciente;
import org.apache.log4j.Logger;

public class AzureDB {
    private static final Logger log = Logger.getLogger(AzureDB.class);
    private final String connectionString;
    private static final String OUTPUT_CODE = "OUTPUT_CODE";
    private static final String MESSAGE = "MESSAGE";

    public AzureDB(){
        this.connectionString = getConnectionString();
    }

    private String getConnectionString(){
        PropertiesConfig properties = new PropertiesConfig();
        return "jdbc:sqlserver://"+ properties.getPropertyConfig("SERVER_BD") + ":1433;" +
                "database="+ properties.getPropertyConfig("BD_NAME") +";" +
                "user="+properties.getPropertyConfig("BD_USERNAME")+"@clinicusildb;" +
                "password="+properties.getPropertyConfig("BD_PASSWORD")+";" +
                "encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    }

    public boolean insertPacienteStatement(Paciente paciente) throws SQLException {
        CallableStatement cstmnt = null;
        Connection cnn = null;
        int outPutCode = 0;
        String message;
        try {
            cnn = DriverManager.getConnection(connectionString);
            cstmnt = cnn.prepareCall("{CALL [dbo].[SP_CREARPACIENTE](?, ?, ?, ?, ?)}");
            cstmnt.setString("ID_PACIENTE", Integer.toString(paciente.getId()));
            cstmnt.setString("NOMBRE", paciente.getNombre());
            cstmnt.setString("DIRECCION", paciente.getDireccion());
            cstmnt.setString("TELEFONO", paciente.getTelefono());
            cstmnt.setString("EMAIL", paciente.getEmail());
            cstmnt.registerOutParameter(OUTPUT_CODE, Types.INTEGER);
            cstmnt.registerOutParameter(MESSAGE, Types.NVARCHAR);
            cstmnt.execute();
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
        return outPutCode == 1;
    }

    public boolean insertDoctorStatement(Doctor doctor) throws SQLException {
        CallableStatement cstmnt = null;
        Connection cnn = null;
        int outPutCode = 0;
        String message;
        try {
            cnn = DriverManager.getConnection(connectionString);
            cstmnt = cnn.prepareCall("{CALL [dbo].[SP_CREARDOCTOR](?, ?, ?, ?, ?, ?, ?, ?)}");
            cstmnt.setString("ID_DOCTOR", Integer.toString(doctor.getId()));
            cstmnt.setInt("ID_ESPECIALIDAD", doctor.getIdEspecialidad());
            cstmnt.setString("NOMBRE", doctor.getNombre());
            cstmnt.setString("DIRECCION", doctor.getDireccion());
            cstmnt.setString("TELEFONO", doctor.getTelefono());
            cstmnt.setString("EMAIL", doctor.getEmail());
            cstmnt.registerOutParameter(OUTPUT_CODE, Types.INTEGER);
            cstmnt.registerOutParameter(MESSAGE, Types.NVARCHAR);
            cstmnt.execute();
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
        return outPutCode == 1;
    }


}
