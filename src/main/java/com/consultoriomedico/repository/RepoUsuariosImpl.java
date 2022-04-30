package com.consultoriomedico.repository;

import com.consultoriomedico.domain.Doctor;
import com.consultoriomedico.domain.Paciente;
import com.consultoriomedico.domain.Usuario;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import lombok.Builder;
import org.apache.log4j.Logger;

@Builder
public class RepoUsuariosImpl implements RepoUsuarios {
    public static final String USUARIO_TXT = "usuario.txt";
    public static final Logger log = Logger.getLogger(RepoUsuariosImpl.class);
    private static final DateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    public void grabar(Object object) {
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
                usuarioTxt.append(";");
                usuarioTxt.append(dt1.format(usuario.getCreadoEn()));
                usuarioTxt.append(";");
                usuarioTxt.append(String.valueOf(flagDoctorNumber));
                usuarioTxt.append(";");
                usuarioTxt.append(usuario.getNombre());
                usuarioTxt.append(";");
                usuarioTxt.append(usuario.getDireccion());
                usuarioTxt.append(";");
                usuarioTxt.append(usuario.getTelefono());
                usuarioTxt.append(";");
                usuarioTxt.append(usuario.getEmail());
                if (usuario.isFlagDoctor()) {
                    usuarioTxt.append(";");
                    assert usuario instanceof Doctor;
                    usuarioTxt.append(((Doctor) usuario).getEspecialidad());
                }
                sendMailConfirmation(usuario);
                //SmsSender.builder().build().sendSms(usuario.getTelefono(), "Se creo el usuario con Exito, KodigoClinica");
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

    public List<Doctor> listarDoctores() {
        ArrayList<Doctor> listaDoctores = new ArrayList<>();

        try (BufferedReader usuariotTxt = new BufferedReader(new FileReader((USUARIO_TXT)))) {
            String line;
            while ((line = usuariotTxt.readLine()) != null) {
                String[] partesDeUsuario = line.split(";");
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
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return listaDoctores;
    }

    public List<Paciente> listarPacientes() {
        ArrayList<Paciente> listaPacientes = new ArrayList<>();

        try (BufferedReader usuariotTxt = new BufferedReader(new FileReader((USUARIO_TXT)))) {
            String line;
            while ((line = usuariotTxt.readLine()) != null) {
                String[] partesDeUsuario = line.split(";");
                if (partesDeUsuario.length > 1) {
                    boolean flagDoctor = Integer.parseInt(partesDeUsuario[2]) == 1;
                    if (!flagDoctor) {
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
        return listaPacientes;
    }

    public List<Doctor> listarDoctoresPorEspeciliadad(String especialidad) {
        ArrayList<Doctor> listDoctores = (ArrayList<Doctor>) listarDoctores();
        ArrayList<Doctor> listDoctoresEspecialidad = new ArrayList<>();
        for (Doctor doctor : listDoctores) {
            if (doctor.getEspecialidad().equalsIgnoreCase(especialidad.toLowerCase()))
                listDoctoresEspecialidad.add(doctor);
        }
        return listDoctoresEspecialidad;
    }

    public Usuario buscarPorId(int id) {
        Usuario usuario = null;
        if (new File(USUARIO_TXT).exists()) {
            try (BufferedReader usuarioTxt = new BufferedReader(new FileReader((USUARIO_TXT)))) {
                String line;
                while ((line = usuarioTxt.readLine()) != null) {
                    String[] partesDeUsuario = line.split(";");
                    if (partesDeUsuario.length > 1 && id == Integer.parseInt(partesDeUsuario[0])) {
                        usuario = Usuario.builder()
                                .id(Integer.parseInt(partesDeUsuario[0]))
                                .creadoEn(dt1.parse(partesDeUsuario[1]))
                                .flagDoctor(Integer.parseInt(partesDeUsuario[2]) == 1)
                                .nombre(partesDeUsuario[3])
                                .direccion(partesDeUsuario[4])
                                .telefono(partesDeUsuario[5])
                                .email(partesDeUsuario[6])
                                .build();
                        break;
                    }
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        return usuario;
    }

    public Doctor buscarDoctorPorId(int id) {
        Doctor doctor = null;
        try(BufferedReader lines = new BufferedReader(new FileReader(USUARIO_TXT))){
            String line;
            while ((line = lines.readLine()) != null){
                String[] arrayDatos = line.split(";");
                if(arrayDatos.length > 1 && Integer.parseInt(arrayDatos[2]) == 1 && Integer.parseInt(arrayDatos[0]) == id){
                    doctor = Doctor.builder().
                            id(Integer.parseInt(arrayDatos[0]))
                            .creadoEn(dt1.parse(arrayDatos[1]))
                            .flagDoctor(true)
                            .nombre(arrayDatos[3])
                            .direccion(arrayDatos[4])
                            .telefono(arrayDatos[5])
                            .email(arrayDatos[6])
                            .especialidad(arrayDatos[7])
                            .build();
                    break;
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return doctor;
    }
}