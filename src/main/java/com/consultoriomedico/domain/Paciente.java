package com.consultoriomedico.domain;

import lombok.*;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor

public class Paciente extends Usuario {
    public Paciente (int id, Date creadoEn, Boolean flagDoctor, String nombre, String direccion, String telefono, String email){
        super(id, creadoEn, flagDoctor, nombre, direccion, telefono, email);
    }
}
