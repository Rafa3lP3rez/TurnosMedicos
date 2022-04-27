package com.consultoriomedico.domain;

import lombok.*;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doctor extends Usuario{
    private String especialidad;

    public Doctor(int id, Date creadoEn, Boolean flagDoctor, String nombre, String direccion, String telefono, String email, String especialidad){
        super(id, creadoEn, flagDoctor, nombre, direccion, telefono, email);
        this.especialidad = especialidad;
    }
}