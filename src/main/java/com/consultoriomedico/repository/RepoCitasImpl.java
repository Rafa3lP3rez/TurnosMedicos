package com.consultoriomedico.repository;

import com.consultoriomedico.domain.Cita;
import com.consultoriomedico.domain.Doctor;
import com.consultoriomedico.domain.Paciente;
import com.consultoriomedico.domain.Usuario;
import lombok.Builder;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Builder
public class RepoCitasImpl implements RepoCitas {

    public static final String CITA_TXT = "citas.txt";
    public static final Logger log = Logger.getLogger(RepoCitasImpl.class);
    private static final DateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    public void grabar(Cita cita) {
        log.info("[RepoCitaImpl][grabar] Inicio de llamada grabación de la cita");
        Cita cita1;

        cita1 = Cita.builder().id(cita.getId_cita())
                .id_doctor(cita.getId_doctor())
                .id_paciente(cita.getId_paciente())
                .fecha(cita.getFecha())
                .creadoEn(cita.getCreadoEn())
                .build();


        if (cita1 != null) {

            try (BufferedWriter citaTxt = new BufferedWriter(new FileWriter(CITA_TXT, true))) {
                citaTxt.newLine();
                citaTxt.append(String.valueOf(cita1.getId_cita()));
                citaTxt.append("; ");
                citaTxt.append(String.valueOf(cita1.getId_doctor()));
                citaTxt.append("; ");
                citaTxt.append(String.valueOf(cita1.getId_paciente()));
                citaTxt.append("; ");
                citaTxt.append(dt1.format(cita1.getFecha()));
                citaTxt.append("; ");
                citaTxt.append(dt1.format(cita1.getCreadoEn()));

                //sendMailConfirmation(cita1);

            } catch (Exception e) {
                log.error(e);
                log.info("[RepoUsuariosImpl][grabar] Error en la grabación");
            } finally {
                log.info("[RepoUsuariosImpl][grabar] Fin de llamada grabación usuario");
            }
        }
    }


    @Override
    public void listarPorDoctor(Object[] doctores) {


    }

    @Override
    public void listarPorPaciente(Object[] pacientes) {

    }

    @Override
    public Usuario buscar(Long id) {
        return null;
    }

    public static void sendMailConfirmation(Usuario usuario) {
        try {
            log.info("[RepoCitaImpl][sendMail] Enviando correo de confirmación");
            EmailSender sender = EmailSender.builder().build();
            sender.sendMail(usuario, "Correo Confirmación de Registro");
            log.info("[RepoUsuariosImpl][sendMail] Correo enviado exitosamente de confirmación");
        } catch (Exception e) {
            log.error(e);
            log.info("[RepoCitaImpl][sendMail] Error al enviar el correo de confirmación");
        }
    }
}
