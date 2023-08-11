package ar.com.unont.dato5.service;

import java.time.LocalDate;
import java.util.List;

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
        eventosRepository.insertOrUpdateEvento(evento.getEventoId(), evento.getDni(),evento.getFechaEntrada(), evento.getPago());
    }

     
    @Override
    @Transactional
    public List<Eventos> selectEventosDelDia() {
        LocalDate fechaActual = LocalDate.now();
        return eventosRepository.findByFechaEntrada(fechaActual);
    }

/*
    @Override
    @Transactional
    public List<Eventos> selectEventosDelDiaSinFechaEntrada() {
        LocalDate fechaActual = LocalDate.now();
        return eventosRepository.findByFechaEntradaIsNullAndFechaActual(fechaActual);
    }
    */

    @Override
    public List<Eventos> getUltimos200EventosSinFechaEntrada() {
        return eventosRepository.findTop200ByFechaEntradaIsNullOrderByEventoIdDesc();
    }

    @Override
    public List<Eventos> eventoSinHoraEntrada() {
        LocalDate fechaActual = LocalDate.now();
        return eventosRepository.findByFechaEntradaAndHoraEntradaIsNull(fechaActual);
    }

}