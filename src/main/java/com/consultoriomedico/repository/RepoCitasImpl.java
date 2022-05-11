package com.consultoriomedico.repository;

import com.consultoriomedico.domain.*;
import lombok.Builder;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Builder
public class RepoCitasImpl implements RepoCitas {

    public static final String CITA_TXT = "citas.txt";
    public static final Logger log = Logger.getLogger(RepoCitasImpl.class);
    private static final DateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    public void grabarCita(Cita cita) {
        log.info("[RepoCitaImpl][grabarCita] Inicio de llamada grabación de la cita");
        try {
            IAzureDB azureDB = new AzureDB();
            azureDB.insertCita(cita);
            sendMailConfirmation(cita);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            log.info("[RepoCitaImpl][grabarCita] Error grabando la cita");
        } finally {
            log.info("[RepoCitaImpl][grabarCita] Fin de llamada grabar cita");
        }

    }

    public List<Cita> listarCitasPorDoctor(Doctor doctor) {
        //TODO: LISTAR CITAS
        return Collections.emptyList();
    }


    public List<Cita> listarCitasPorPaciente(int idPaciente) {
        log.info("[RepoCitasImpl][listarCitasPorPaciente] Inicio de llamada listar horario:");
        List<Cita> listCita = null;
        try {
            IAzureDB azureDB = new AzureDB();
            listCita = azureDB.processResultSetListPorPacienteCita(idPaciente);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            log.info("[RepoCitasImpl][listarCitasPorPaciente] Error listando");
        } finally {
            log.info("[RepoCitasImpl][listarCitasPorPaciente] Fin de llamada listar cita por paciente");
        }
        return listCita;
    }

    public void cancelarCita(int idCita){
        //TODO: Cancelar una cita
    }

    public List<Horario> listarHorariosDisponibles(int idEspecialidad, String fecha){
        log.info("[RepoCitasImpl][listarHorariosDisponibles] Inicio de llamada listar horario:");
        List<Horario> listHorario = null;
        try {
            IAzureDB azureDB = new AzureDB();
            listHorario = azureDB.listHorarioDisponiblesXRecomendacion(idEspecialidad, fecha);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            log.info("[RepoCitasImpl][listarHorariosDisponibles] Error listando");
        } finally {
            log.info("[RepoCitasImpl][listarHorariosDisponibles] Fin de llamada buscar doctor por Id");
        }
        return listHorario;
    }

    public Cita buscarPorId(int id){
        //TODO: RETORNAR CITA POR ID
        return Cita.builder().build();
    }

    public void sendMailConfirmation(Cita cita) {
        try {
            log.info("[RepoCitaImpl][sendMail] Enviando correo de confirmación");
            EmailSender sender = EmailSender.builder().build();
            sender.enviarConfirmacionCita(cita);
            log.info("[RepoCitaImpl][sendMail] Correo enviado exitosamente de confirmación");
        } catch (Exception e) {
            log.error(e);
            log.info("[RepoCitaImpl][sendMail] Error al enviar el correo de confirmación");
        }
    }


}
