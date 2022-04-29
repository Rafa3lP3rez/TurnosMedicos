package com.consultoriomedico.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
public class Entidad {
    private int id;
    private Date creadoEn;
}