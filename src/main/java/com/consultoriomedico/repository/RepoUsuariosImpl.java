package com.consultoriomedico.repository;

import com.consultoriomedico.domain.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RepoUsuariosImpl implements RepoUsuarios {
    public static final String USUARIO_TXT = "usuario.txt";

    public void grabar(Usuario usuario) throws IOException {
        //FileWriter usuarioTxt = null;
        //try {
        //    usuarioTxt = new FileWriter(USUARIO_TXT);
        //} finally {
        //    if (usuarioTxt != null) {
        //        usuarioTxt.close();
        //    }
        //}
        try (BufferedWriter usuarioTxt = new BufferedWriter(new FileWriter(USUARIO_TXT))) {
            usuarioTxt.write(usuario.getId());
            usuarioTxt.write("; ");
            usuarioTxt.write(usuario.getNombre());
            usuarioTxt.write("; ");
            usuarioTxt.write(usuario.getEmail());
            usuarioTxt.write("; ");
            usuarioTxt.write(usuario.getTelefono());
            usuarioTxt.write("; ");
            usuarioTxt.write(usuario.getDireccion());
            usuarioTxt.write("; ");
            usuarioTxt.write((int) usuario.getCreadoEn().getTime());
            usuarioTxt.newLine();
        }
    }

    public List<Usuario> listar() throws IOException {
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
            }
        }
        return usuarios;
    }

    public List<Usuario> buscarPorId(int id) throws IOException {
        List<Usuario> usuarios = new ArrayList<>();
        //while (usuarios != null) {
        //    if (id == null) {
        //        throw new IOException();
        //    }
        //}
        try (BufferedReader usuariotTxt = new BufferedReader(new FileReader((USUARIO_TXT)))) {
            String lectura = usuariotTxt.readLine();
            while (lectura != null) {
                String[] partesDeUsuario = lectura.split("; ");
                Usuario usuario = new Usuario();
                id = Integer.parseInt(partesDeUsuario[0]);
                usuarios.get(id);
                lectura = usuariotTxt.readLine();
            }
        }
        return this.buscarPorId(id);
    }
}