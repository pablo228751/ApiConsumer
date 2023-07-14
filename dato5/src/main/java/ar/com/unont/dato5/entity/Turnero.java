package ar.com.unont.dato5.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "turnero")
public class Turnero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("Pagina")
    private int pagina;

    @JsonProperty("Registros")
    private int registros;

    @JsonProperty("TotalPaginas")
    private int totalPaginas;


    @JsonProperty("Resultados")
    @OneToMany(mappedBy = "turnero", cascade = CascadeType.ALL, orphanRemoval = true)
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
