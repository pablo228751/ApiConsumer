package ar.com.unont.dato5.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Turno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long turnoId;
    private String nombreCompleto;
    private String tramite;
    private String fechaTurno;
    private String centroAtencion;
    private String email;
    private String dni;
}
