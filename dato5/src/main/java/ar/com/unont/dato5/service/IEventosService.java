package ar.com.unont.dato5.service;

import java.util.List;

import ar.com.unont.dato5.entity.Eventos;

public interface IEventosService {
    void insertarEvento(Eventos evento);
    List<Eventos> selectEventosDelDia();
    //List<Eventos> selectEventosDelDiaSinFechaEntrada();
    List<Eventos> getUltimos200EventosSinFechaEntrada();
    List<Eventos> eventoSinHoraEntrada();
}