package com.consultoriomedico.repository;

import com.consultoriomedico.domain.Usuario;

import java.io.IOException;
import java.util.List;

public interface RepoUsuarios {
    void grabar(Usuario usuario) throws IOException;

    List<Usuario> listar() throws IOException;

    List<Usuario> buscarPorId(int id) throws IOException;

}
