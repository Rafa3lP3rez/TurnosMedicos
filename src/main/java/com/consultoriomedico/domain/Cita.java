package com.consultoriomedico.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@ToString(callSuper = true)
public class Cita extends Entidad{
    private Paciente paciente;
    private Doctor doctor;
    private Horario horario;
}
