package ar.com.unont.dato5.service;

import ar.com.unont.dato5.entity.Turnero;
import ar.com.unont.dato5.entity.Turno;
import ar.com.unont.dato5.repository.ITurneroRepository;
import ar.com.unont.dato5.repository.ITurnoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TurneroServiceImpl implements ITurneroService {

    private final ITurneroRepository turneroRepository;
    private final ITurnoRepository turnoRepository;

    public TurneroServiceImpl(ITurneroRepository turneroRepository, ITurnoRepository turnoRepository) {
        this.turneroRepository = turneroRepository;
        this.turnoRepository = turnoRepository;
    }

    @Override
    public void insertTurnero(Turnero turnero) {
        List<Turno> resultados = turnero.getResultados();
        turneroRepository.save(turnero); // Persistir el objeto Turnero primero para obtener un ID

        for (Turno turno : resultados) {
            turno.setTurnero(turnero); // Establecer la relación Turnero-Turno
            turnoRepository.save(turno); // Persistir el objeto Turno
        }
    }

    @Override
    public List<Turno> selectTurnos() {
        LocalDate fechaActual = LocalDate.now();
        return turnoRepository.findByFechaActualizacion(fechaActual);
    }

}
