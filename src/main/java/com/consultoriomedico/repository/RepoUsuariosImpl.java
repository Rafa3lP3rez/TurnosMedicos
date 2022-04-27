package com.consultoriomedico.repository;

import com.consultoriomedico.domain.Doctor;
import com.consultoriomedico.domain.EmailSender;
import com.consultoriomedico.domain.Paciente;
import com.consultoriomedico.domain.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;

public class RepoUsuariosImpl implements RepoUsuarios {
    public static final String USUARIO_TXT = "usuario.txt";
    public static final Logger log = Logger.getLogger(RepoUsuariosImpl.class);

    public static void grabar(Object object) throws IOException {
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
        log.info("[RepoUsuariosImpl][grabar] Inicio de llamada grabación usuario");
        Usuario usuario;
        if (object instanceof Paciente paciente){
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

        if (usuario != null){
            int flagDoctorNumber = usuario.isFlagDoctor() ? 1:0;
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
                if (usuario.isFlagDoctor()){
                    usuarioTxt.append("; ");
                    assert usuario instanceof Doctor;
                    usuarioTxt.append(((Doctor) usuario).getEspecialidad());
                }
                sendMail(usuario);
            } catch(Exception e) {
                log.error(e);
                log.info("[RepoUsuariosImpl][grabar] Error en la grabación");
            } finally {
                log.info("[RepoUsuariosImpl][grabar] Fin de llamada grabación usuario");
            }
        }
    }

    public static void sendMail(Usuario usuario){
        try {
            log.info("[RepoUsuariosImpl][sendMail] Enviando correo de confirmación");
            EmailSender sender = EmailSender.builder().build();
            sender.sendMail(usuario.getEmail(), "Correo Prueba", "Este es un correo de prueba");
        }catch(Exception e) {
            log.error(e);
            log.info("[RepoUsuariosImpl][sendMail] Error al enviar el correo de confirmación");
        }
    }

    public List<Usuario> listar() throws IOException {
        return new ArrayList<>();
    }

    public Usuario buscarPorId(int id) throws IOException {
        return Usuario.builder().build();
    }


}