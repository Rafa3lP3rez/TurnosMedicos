package com.consultoriomedico.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario extends Entidad {

    @Id
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
}
