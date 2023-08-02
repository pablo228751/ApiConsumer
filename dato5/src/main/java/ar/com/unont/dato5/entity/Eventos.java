package ar.com.unont.dato5.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "eventos")
@Data
public class Eventos {

    @Id
    private Long eventoId;
    private int barreraEntrada;
    private String tarjeta;
    private String dni;
    private char e_s;
    private LocalDate fechaEntrada;
    private LocalTime horaEntrada;
    private String pago;
    private LocalDate fechaSalida;
    private int barreraSalida;

    @Override
    public String toString() {
        return "Eventos{" +
                "eventoId=" + eventoId +
                ", barreraEntrada=" + barreraEntrada +
                ", tarjeta='" + tarjeta + '\'' +
                ", dni='" + dni + '\'' +
                ", e_s=" + e_s +
                ", fechaEntrada=" + fechaEntrada +
                ", horaEntrada=" + horaEntrada +
                ", pago=" + pago +
                ", fechaSalida=" + fechaSalida +
                ", barreraSalida=" + barreraSalida +
                '}';
    }
}
