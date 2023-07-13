package ar.com.unont.dato5.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "turnero")
public class Turnero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int pagina;
    private int totalPaginas;
    private int registros;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Turno> resultados;

    @Override
    public String toString() {
        return "Turnero{" +
                "id=" + id +
                ", pagina=" + pagina +
                ", totalPaginas=" + totalPaginas +
                ", registros=" + registros +
                ", resultados=" + resultados +
                '}';
    }
}
