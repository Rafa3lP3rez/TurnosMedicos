package com.consultoriomedico.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Horario {
    private String idDoctor;
    private String fecha;
    private String horaInicio;
    private String horaFin;
}
