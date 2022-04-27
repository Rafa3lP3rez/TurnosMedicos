package com.consultoriomedico.repository;

import com.consultoriomedico.domain.Doctor;
import com.consultoriomedico.domain.Paciente;
import com.consultoriomedico.domain.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;

public class RepoUsuariosImpl implements RepoUsuarios {
    public static final String USUARIO_TXT = "usuario.txt";
    public static final Logger log = Logger.getLogger(RepoUsuariosImpl.class);
    private static final SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");


    public static void grabar(Object object) throws IOException {
        log.info("[RepoUsuariosImpl][grabar] Inicio de llamada grabación usuario");
        Usuario usuario;
        if (object instanceof Paciente paciente){
            usuario = new Paciente(
                    (paciente).getId(),
                    (paciente).getCreadoEn(),
                    (paciente).isFlagDoctor(),
                    (paciente).getNombre(),
                    (paciente).getDireccion(),
                    (paciente).getTelefono(),
                    (paciente).getEmail()
            );
        } else if (object instanceof Doctor doctor) {
            usuario = new Doctor(
                    (doctor).getId(),
                    (doctor).getCreadoEn(),
                    (doctor).isFlagDoctor(),
                    (doctor).getNombre(),
                    (doctor).getDireccion(),
                    (doctor).getTelefono(),
                    (doctor).getEmail(),
                    (doctor).getEspecialidad()
            );
        } else {
            usuario = null;
        }

        if (usuario != null){
            int flagDoctorNumber = usuario.isFlagDoctor() ? 1:0;
            try (BufferedWriter usuarioTxt = new BufferedWriter(new FileWriter(USUARIO_TXT, true))) {
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
                if (((Usuario) object).isFlagDoctor()){
                    usuarioTxt.append("; ");
                    assert usuario instanceof Doctor;
                    usuarioTxt.append(((Doctor) usuario).getEspecialidad());
                    usuarioTxt.append("; ");
                }
                usuarioTxt.newLine();
            } catch(Exception e) {
                log.error(e);
            } finally {
                log.info("[RepoUsuariosImpl][grabar] Fin de llamada grabación usuario");
            }
        }
    }

    public List<Usuario> listar() throws IOException {
        //TODO: ARREGLAR IMPLEMENTACIÓN//
        List<Usuario> usuarios = new ArrayList<>();
        try (BufferedReader usuariotTxt = new BufferedReader(new FileReader((USUARIO_TXT)))) {
            String lectura = usuariotTxt.readLine();
            while (lectura != null) {
                String[] partesDeUsuario = lectura.split("; ");
                Usuario usuario = new Usuario();
                usuario.setId(Integer.parseInt(partesDeUsuario[0]));
                usuario.setNombre((partesDeUsuario[1]));
                usuario.setEmail((partesDeUsuario[2]));
                usuario.setTelefono((partesDeUsuario[3]));
                usuario.setDireccion((partesDeUsuario[4]));
                usuario.setCreadoEn(new Date(Integer.parseInt(partesDeUsuario[5])));
                usuarios.add(usuario);
                lectura = usuariotTxt.readLine();
                //Comentario para commit
            }
        }
        return usuarios;
    }

    public Usuario buscarPorId(int id) throws IOException {
        //TODO: ARREGLAR IMPLEMENTACIÓN//
        Usuario usuario = null;
        try (BufferedReader usuariotTxt = new BufferedReader(new FileReader((USUARIO_TXT)))) {
            String lectura = usuariotTxt.readLine();
            while (lectura != null) {
                String[] partesDeUsuario = lectura.split("; ");
                if (id == Integer.parseInt(partesDeUsuario[0])) {
                    usuario.setNombre((partesDeUsuario[1]));
                    usuario.setEmail((partesDeUsuario[2]));
                    usuario.setTelefono((partesDeUsuario[3]));
                    usuario.setDireccion((partesDeUsuario[4]));
                    usuario.setCreadoEn(new Date(Integer.parseInt(partesDeUsuario[5])));
                }
                lectura = usuariotTxt.readLine();
            }
        }
        return usuario;
    }
}