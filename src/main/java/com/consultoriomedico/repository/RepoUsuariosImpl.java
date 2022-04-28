package com.consultoriomedico.repository;

import com.consultoriomedico.domain.Doctor;
import com.consultoriomedico.domain.EmailSender;
import com.consultoriomedico.domain.Paciente;
import com.consultoriomedico.domain.Usuario;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

public class RepoUsuariosImpl implements RepoUsuarios {
    public static final String USUARIO_TXT = "usuario.txt";
    public static final Logger log = Logger.getLogger(RepoUsuariosImpl.class);
    private static final DateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    private RepoUsuariosImpl(){

    }

    public static void grabar(Object object) throws IOException {
        log.info("[RepoUsuariosImpl][grabar] Inicio de llamada grabación usuario");
        Usuario usuario;
        if (object instanceof Paciente paciente) {
            usuario = Paciente.builder().id(paciente.getId())
                    .creadoEn(paciente.getCreadoEn())
                    .flagDoctor(paciente.isFlagDoctor())
                    .nombre(paciente.getNombre())
                    .direccion(paciente.getDireccion())
                    .telefono(paciente.getTelefono())
                    .email(paciente.getEmail())
                    .build();
        } else if (object instanceof Doctor doctor) {
            usuario = Doctor.builder().id(doctor.getId())
                    .creadoEn(doctor.getCreadoEn())
                    .especialidad(doctor.getEspecialidad())
                    .flagDoctor(doctor.isFlagDoctor())
                    .nombre(doctor.getNombre())
                    .direccion(doctor.getDireccion())
                    .telefono(doctor.getTelefono())
                    .email(doctor.getEmail()).build();
        } else {
            usuario = null;
        }

        if (usuario != null) {
            int flagDoctorNumber = usuario.isFlagDoctor() ? 1 : 0;
            try (BufferedWriter usuarioTxt = new BufferedWriter(new FileWriter(USUARIO_TXT, true))) {
                usuarioTxt.newLine();
                usuarioTxt.append(String.valueOf(usuario.getId()));
                usuarioTxt.append("; ");
                usuarioTxt.append(dt1.format(usuario.getCreadoEn()));
                usuarioTxt.append("; ");
                usuarioTxt.append(String.valueOf(flagDoctorNumber));
                usuarioTxt.append("; ");
                usuarioTxt.append(usuario.getNombre());
                usuarioTxt.append("; ");
                usuarioTxt.append(usuario.getDireccion());
                usuarioTxt.append("; ");
                usuarioTxt.append(usuario.getTelefono());
                usuarioTxt.append("; ");
                usuarioTxt.append(usuario.getEmail());
                if (usuario.isFlagDoctor()) {
                    usuarioTxt.append("; ");
                    assert usuario instanceof Doctor;
                    usuarioTxt.append(((Doctor) usuario).getEspecialidad());
                }
                sendMailConfirmation(usuario);
            } catch (Exception e) {
                log.error(e);
                log.info("[RepoUsuariosImpl][grabar] Error en la grabación");
            } finally {
                log.info("[RepoUsuariosImpl][grabar] Fin de llamada grabación usuario");
            }
        }
    }

    public static void sendMailConfirmation(Usuario usuario) {
        try {
            log.info("[RepoUsuariosImpl][sendMail] Enviando correo de confirmación");
            EmailSender sender = EmailSender.builder().build();
            sender.sendMail(usuario, "Correo Confirmación de Registro");
            log.info("[RepoUsuariosImpl][sendMail] Correo enviado exitosamente de confirmación");
        } catch (Exception e) {
            log.error(e);
            log.info("[RepoUsuariosImpl][sendMail] Error al enviar el correo de confirmación");
        }
    }

    public static Object[] listarUsuarios() {
        List<Doctor> listaDoctores = new ArrayList<>();
        List<Paciente> listaPacientes = new ArrayList<>();
        try (BufferedReader usuariotTxt = new BufferedReader(new FileReader((USUARIO_TXT)))) {
            String line;
            while ((line = usuariotTxt.readLine()) != null) {
                String[] partesDeUsuario = line.split("; ");
                if (partesDeUsuario.length > 1) {
                    boolean flagDoctor = Integer.parseInt(partesDeUsuario[2]) == 1;
                    if (flagDoctor) {
                        Doctor doctor = Doctor.builder()
                                .id(Integer.parseInt(partesDeUsuario[0]))
                                .creadoEn(dt1.parse(partesDeUsuario[1]))
                                .flagDoctor(true)
                                .nombre(partesDeUsuario[3])
                                .direccion(partesDeUsuario[4])
                                .telefono(partesDeUsuario[5])
                                .email(partesDeUsuario[6])
                                .especialidad(partesDeUsuario[7])
                                .build();

                        listaDoctores.add(doctor);
                    } else {
                        Paciente paciente = Paciente.builder()
                                .id(Integer.parseInt(partesDeUsuario[0]))
                                .creadoEn(dt1.parse(partesDeUsuario[1]))
                                .flagDoctor(false)
                                .nombre(partesDeUsuario[3])
                                .direccion(partesDeUsuario[4])
                                .telefono(partesDeUsuario[5])
                                .email(partesDeUsuario[6])
                                .build();
                        listaPacientes.add(paciente);
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return new Object[]{listaDoctores, listaPacientes};
    }

    public static Usuario buscarPorId(int id) {
        Usuario usuario = null;
        try (BufferedReader usuariotTxt = new BufferedReader(new FileReader((USUARIO_TXT)))) {
            String line;
            while ((line = usuariotTxt.readLine()) != null) {
                String[] partesDeUsuario = line.split("; ");
                if (partesDeUsuario.length > 1 && id == Integer.parseInt(partesDeUsuario[0])) {
                    usuario = Usuario.builder()
                            .id(Integer.parseInt(partesDeUsuario[0]))
                            .creadoEn(dt1.parse(partesDeUsuario[1]))
                            .flagDoctor(false)
                            .nombre(partesDeUsuario[3])
                            .direccion(partesDeUsuario[4])
                            .telefono(partesDeUsuario[5])
                            .email(partesDeUsuario[6])
                            .build();
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return usuario;
    }
}