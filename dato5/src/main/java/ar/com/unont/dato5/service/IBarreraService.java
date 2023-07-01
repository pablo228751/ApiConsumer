package ar.com.unont.dato5.service;

import java.util.Optional;

import ar.com.unont.dato5.entity.Barrera;

public interface IBarreraService {

    Barrera guardar(Barrera barrera);
    Optional<Barrera> buscarPorId(int id);
    Barrera actualizar(int id, Barrera barrera);
    void mostrar();
       
}
