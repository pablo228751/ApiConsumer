package ar.com.unont.dato5.service;

import java.util.List;
import ar.com.unont.dato5.entity.TipoCliente;

public interface ITipoClienteService {
    List<TipoCliente> seleccionarTodo();
    void actualizarTipoCliente(TipoCliente tipoCliente);
    void insertarTipoCliente(TipoCliente tipoCliente);
    void actualizarEstadoTipoCliente(Long id, int estado);
    List<TipoCliente> seleccionarPorEstados(List<Integer> estados);
}