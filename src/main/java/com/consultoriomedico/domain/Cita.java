package com.consultoriomedico.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Time;
import java.util.Date;

@Data
@SuperBuilder
public class Cita extends Entidad {
    private Date fecha;
}
