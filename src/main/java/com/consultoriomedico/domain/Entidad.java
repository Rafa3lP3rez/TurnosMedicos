package com.consultoriomedico.domain;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Entidad {
    private int id;
    private Date creadoEn;
}