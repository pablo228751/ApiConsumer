package ar.com.unont.dato5.entity;


import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name = "tipo_cliente")
@Data
public class TipoCliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "tarjeta")
    private String tarjeta;

    @Column(name = "legajo")
    private String legajo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "e_s")
    private char e_s;

    @Column(name = "nroorden")
    private String nroorden;

    @Column(name = "fechahora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechahora;

    @Column(name = "usuario")
    private String usuario;

    @Column(name = "estado")
    private int estado;

    @Column(name = "retiro")
    private boolean retiro;

    @Column(name = "nrocuenta")
    private int nrocuenta;

    @Column(name = "id_tc")
    private double id_tc;

    @Column(name = "observacion")
    private String observacion;

    @Column(name = "vto")
    @Temporal(TemporalType.DATE)
    private Date vto;

    @Column(name = "tipo")
    private char tipo;

    @Column(name = "activo")
    private boolean activo;

}
