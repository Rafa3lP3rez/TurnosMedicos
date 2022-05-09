package com.consultoriomedico.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Especialidad {
    private int idEspecialidad;
    private String nombreEspecialidad;
}
