package ar.com.unont.dato5.service;

import ar.com.unont.dato5.entity.Turnero;
import ar.com.unont.dato5.repository.ITurneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TurneroServiceImpl implements ITurneroService {

    @Autowired
    private final ITurneroRepository turneroRepository;

    public TurneroServiceImpl(ITurneroRepository turneroRepository) {
        this.turneroRepository = turneroRepository;
    }
    @Override
    public void guardar(Turnero turnero) {
        this.turneroRepository.save(turnero);
    }
}
