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

    public void grabar(Cita cita, Usuario usuario, Doctor doctor) {
        log.info("[RepoCitaImpl][grabar] Inicio de llamada grabación de la cita");

        if (cita != null) {
            try (BufferedWriter citaTxt = new BufferedWriter(new FileWriter(CITA_TXT, true))) {
                citaTxt.newLine();
                citaTxt.append(String.valueOf(cita.getIdCita()));
                citaTxt.append(";");
                citaTxt.append(String.valueOf(cita.getIdDoctor()));
                citaTxt.append(";");
                citaTxt.append(String.valueOf(cita.getIdPaciente()));
                citaTxt.append(";");
                citaTxt.append(dt1.format(cita.getFecha()));
                citaTxt.append(";");
                citaTxt.append(dt1.format(cita.getCreadoEn()));
                sendMailConfirmation(usuario, cita, doctor);

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


    public List<Cita> listarCitasPorPaciente(Paciente paciente) {
        log.info(String.format("[RepoCitasImpl][listarPorPaciente] Listando citas por paciente, paciente recibido -> %s", paciente));
        List<Cita> listaPacientes = new ArrayList<>();
        try (BufferedReader lines = new BufferedReader(new FileReader(CITA_TXT))){
            String line;
            while((line = lines.readLine()) != null){
                String[] parteCitas = line.split(";");
                if (parteCitas.length > 1 && Integer.parseInt(parteCitas[2]) == paciente.getId()) {
                    listaPacientes.add(Cita.builder()
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
        }catch (IOException | ParseException e){
            log.error(e);
        }
        return listaPacientes;
    }

    public Usuario buscar(Long id) {
        return null;
    }

    public void sendMailConfirmation(Usuario usuario, Cita cita, Doctor doctor) {
        try {
            log.info("[RepoCitaImpl][sendMail] Enviando correo de confirmación");
            EmailSender sender = EmailSender.builder().build();
            sender.enviarConfirmacionCita(usuario, cita, doctor);
            log.info("[RepoCitaImpl][sendMail] Correo enviado exitosamente de confirmación");
        } catch (Exception e) {
            log.error(e);
            log.info("[RepoCitaImpl][sendMail] Error al enviar el correo de confirmación");
        }
    }
}
