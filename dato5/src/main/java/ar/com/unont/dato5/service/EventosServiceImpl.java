package ar.com.unont.dato5.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ar.com.unont.dato5.entity.Eventos;
import ar.com.unont.dato5.repository.IEventosRepository;

@Service
public class EventosServiceImpl implements IEventosService {

    @Autowired
    private final IEventosRepository eventosRepository;

    
    public EventosServiceImpl(IEventosRepository eventosRepository) {
        this.eventosRepository = eventosRepository;
    }

    @Override
    @Transactional
    public void insertarEvento(Eventos evento) {
        eventosRepository.insertOrUpdateEvento(evento.getEventoId(), evento.getDni(), evento.getPago());
    }

}