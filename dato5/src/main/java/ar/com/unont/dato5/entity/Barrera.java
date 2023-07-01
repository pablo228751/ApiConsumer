package ar.com.unont.dato5.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Barrera {
    @Id
    private int id;
    @Column
    private String descripcion;
    @Column
    private String detalles;
    @Column
    private int activa;
    @Column
    private String ip;
    @Column
    private String ingegr;
    @Column
    private String base;
    @Column
    private String usuario;
    @Column
    private String clave;
    @Column
    private String puerto;
    @Column
    private int nrobarrera;

    
}
