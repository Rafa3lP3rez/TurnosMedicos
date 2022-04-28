package com.consultoriomedico.repository;

import com.consultoriomedico.domain.Usuario;
import java.io.IOException;

public interface RepoUsuarios {
    public static void grabar(Object usuario) throws IOException {}

    public static Object[] listarUsuarios(){
        return new Object[]{};
    }

    public static Usuario buscarPorId(){
        return Usuario.builder().build();
    }

}
