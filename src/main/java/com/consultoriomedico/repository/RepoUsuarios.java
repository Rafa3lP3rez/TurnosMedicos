package com.consultoriomedico.repository;

import com.consultoriomedico.domain.Usuario;

public interface RepoUsuarios {

    void grabar(Object usuario);

    Object[] listarUsuarios();

    Usuario buscarPorId(int id);

}
