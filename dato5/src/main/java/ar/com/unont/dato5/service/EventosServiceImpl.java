package ar.com.unont.dato5.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void insertarEvento(Eventos evento) {
        eventosRepository.save(evento);
    }
}