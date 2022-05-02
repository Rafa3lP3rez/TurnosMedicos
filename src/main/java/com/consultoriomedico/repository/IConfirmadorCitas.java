package com.consultoriomedico.repository;

import com.consultoriomedico.domain.Cita;
import com.consultoriomedico.domain.Usuario;

import java.io.IOException;

public interface IConfirmadorCitas {

    boolean enviarConfirmacion(Usuario usuario, Cita cita) throws IOException;
}
