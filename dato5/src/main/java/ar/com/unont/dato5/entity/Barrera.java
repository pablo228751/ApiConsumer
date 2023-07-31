package ar.com.unont.dato5.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "barrera")
public class Barrera implements Serializable {

    @Id
    private Long barrera_id;

    private String descripcion;
    private String detalles;
    private int activa;
    private String ip;
    private String puerto;
    private String ingegr;
    private String base;
    private String usuario;
    private String clave;
    private int nrobarrera;

    @Override
    public String toString() {
        return "Barrera{" +
                "id=" + barrera_id +
                ", descripcion='" + descripcion + '\'' +
                ", detalles='" + detalles + '\'' +
                ", activa=" + activa +
                ", ip='" + ip + '\'' +
                ", puerto='" + puerto + '\'' +
                ", ingegr='" + ingegr + '\'' +
                ", base='" + base + '\'' +
                ", usuario='" + usuario + '\'' +
                ", clave='" + clave + '\'' +
                ", nrobarrera=" + nrobarrera +
                '}';
    }
}