package com.consultoriomedico.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario extends Entidad {

    private boolean flagDoctor;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;


    public Usuario(int id, Date creadoEn, boolean flagDoctor, String nombre, String direccion, String telefono, String email) {
        super(id, creadoEn);
        this.flagDoctor = flagDoctor;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
    }
}
