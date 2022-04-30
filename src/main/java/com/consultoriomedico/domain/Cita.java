package com.consultoriomedico.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;

@Data
@SuperBuilder
@ToString(callSuper = true)
public class Cita extends Entidad {

    private int idCita;
    private int idPaciente;
    private int idDoctor;
    private Date fecha;

}
