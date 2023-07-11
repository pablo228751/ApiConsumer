package ar.com.unont.dato5.entity;

import lombok.Data;

@Data
public class Turno {
    private Long turnoId;
    private String nombreCompleto;
    private String tramite;
    private String fechaTurno;
    private String centroAtencion;
    private String email;
    private String dni;
}
