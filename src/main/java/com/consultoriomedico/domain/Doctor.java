package com.consultoriomedico.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;


@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@ToString(callSuper = true)
public class Doctor extends Usuario {
    private int idEspecialidad;
}