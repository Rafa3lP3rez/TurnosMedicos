package com.consultoriomedico.repository;

import com.consultoriomedico.domain.Cita;
import com.consultoriomedico.domain.Doctor;
import com.consultoriomedico.domain.Paciente;
import com.consultoriomedico.domain.Usuario;
import lombok.Builder;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Builder
public class RepoCitasImpl implements RepoCitas {

    public static final String CITA_TXT = "citas.txt";
    public static final Logger log = Logger.getLogger(RepoCitasImpl.class);
    private static final DateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    public void grabar(Cita cita) {
        log.info("[RepoCitaImpl][grabar] Inicio de llamada grabación de la cita");
        Cita cita1;

        cita1 = Cita.builder().id(cita.getIdCita())
                .idCita(cita.getIdCita())
                .idDoctor(cita.getIdDoctor())
                .idPaciente(cita.getIdPaciente())
                .fecha(cita.getFecha())
                .creadoEn(cita.getCreadoEn())
                .build();


        if (cita1 != null) {

            try (BufferedWriter citaTxt = new BufferedWriter(new FileWriter(CITA_TXT, true))) {
                citaTxt.newLine();
                citaTxt.append(String.valueOf(cita1.getIdCita()));
                citaTxt.append(";");
                citaTxt.append(String.valueOf(cita1.getIdDoctor()));
                citaTxt.append(";");
                citaTxt.append(String.valueOf(cita1.getIdPaciente()));
                citaTxt.append(";");
                citaTxt.append(dt1.format(cita1.getFecha()));
                citaTxt.append(";");
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

    public List<Cita> listarCitasPorDoctor(Doctor doctor) {
        log.info(String.format("[RepoCitasImpl][listarPorDoctor] Listando citas por doctor, doctor recibido -> %s", doctor));
        List<Cita> listCitas = new ArrayList<>();
        try (BufferedReader lines = new BufferedReader(new FileReader(CITA_TXT))){
            String line;
            while((line = lines.readLine()) != null){
                String[] parteCitas = line.split(";");
                if (parteCitas.length > 1 && Integer.parseInt(parteCitas[1]) == doctor.getId()) {
                    listCitas.add(Cita.builder()
                            .id(Integer.parseInt(parteCitas[0]))
                            .creadoEn(dt1.parse(parteCitas[4]))
                            .idCita(Integer.parseInt(parteCitas[0]))
                            .idPaciente(Integer.parseInt(parteCitas[2]))
                            .idDoctor(Integer.parseInt(parteCitas[1]))
                            .fecha(dt1.parse(parteCitas[3]))
                            .build()
                        );
                }
            }
        } catch (IOException | ParseException e) {
            log.error(e);
        }
        return listCitas;
    }

    public void listarPorPaciente(Object[] pacientes) {
    //TODO

    }

    public Usuario buscar(Long id) {
        return null;
    }

    public void sendMailConfirmation(Usuario usuario) {
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
