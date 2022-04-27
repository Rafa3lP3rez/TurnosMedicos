package com.consultoriomedico.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;


@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class Usuario extends Entidad {

    private boolean flagDoctor;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;

}
