package com.consultoriomedico.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;

@Data
@SuperBuilder
public class Cita extends Entidad {

    private int id_cita;
    private int id_paciente;
    private int id_doctor;
    private Date fecha;

}
