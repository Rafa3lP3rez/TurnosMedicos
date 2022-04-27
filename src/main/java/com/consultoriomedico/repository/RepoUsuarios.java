package com.consultoriomedico.repository;

import com.consultoriomedico.domain.Usuario;
import java.io.IOException;
import java.util.List;

public interface RepoUsuarios {
    public static void grabar(Object usuario) throws IOException {}

    List<Usuario> listar() throws IOException;

    Usuario buscarPorId(int id) throws IOException;

}
