package ar.com.unont.dato5.entity;

import java.util.List;

import lombok.Data;

@Data
public class Turnero {
    private Long id;
    private int pagina;
    private int totalPaginas;
    private int registros;
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
