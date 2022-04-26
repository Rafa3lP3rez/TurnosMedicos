package com.consultoriomedico.domain;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Entidad {
    @Id
    private int id;
    private Date creadoEn;
}