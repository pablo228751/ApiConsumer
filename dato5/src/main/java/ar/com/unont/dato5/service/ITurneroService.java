package ar.com.unont.dato5.service;

import java.util.List;

import ar.com.unont.dato5.entity.Turnero;
import ar.com.unont.dato5.entity.Turno;

public interface ITurneroService {
    void insertTurnero(Turnero turnero); 
    List<Turno> selectTurnos();
    
}