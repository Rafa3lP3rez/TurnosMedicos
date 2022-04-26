package com.consultoriomedico.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario extends Entidad {

    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
}
