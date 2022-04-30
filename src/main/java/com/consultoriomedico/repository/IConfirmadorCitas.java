package com.consultoriomedico.repository;

import com.consultoriomedico.domain.Cita;
import com.consultoriomedico.domain.Usuario;

public interface IConfirmadorCitas {

    boolean enviarConfirmacion(Usuario usuario, Cita cita);
}
