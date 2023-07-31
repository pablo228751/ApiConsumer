package ar.com.unont.dato5.entity;

import java.time.LocalDate;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "turno")
public class Turno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty("TurnoId")
    private Long turnoId;
    @JsonProperty("NombreCompleto")
    private String nombreCompleto;
    @JsonProperty("Tramite")
    private String tramite;
    @JsonProperty("FechaTurno")
    private String fechaTurno;
    @JsonProperty("CentroAtencion")
    private String centroAtencion;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("Dni")
    private String dni;
     @ManyToOne
    @JoinColumn(name = "turnero_id")
    private Turnero turnero;
    @UpdateTimestamp
    private LocalDate fechaActualizacion;

    @Override
    public String toString() {
        return "Turno{" +
                "id=" + id +
                ", turnoId=" + turnoId +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", tramite='" + tramite + '\'' +
                ", fechaTurno='" + fechaTurno + '\'' +
                ", centroAtencion='" + centroAtencion + '\'' +
                ", email='" + email + '\'' +
                ", dni='" + dni + '\'' +
                ", fechaActualizacion=" + fechaActualizacion +
                '}';
    }
}