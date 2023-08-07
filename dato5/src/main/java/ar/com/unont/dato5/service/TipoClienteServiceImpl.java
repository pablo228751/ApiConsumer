package ar.com.unont.dato5.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.com.unont.dato5.entity.TipoCliente;
import ar.com.unont.dato5.repository.ITipoClienteRepository;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class TipoClienteServiceImpl implements ITipoClienteService {

    @Autowired
    private final ITipoClienteRepository tipoClienteRepository;

    public TipoClienteServiceImpl(ITipoClienteRepository tipoClienteRepository) {
        this.tipoClienteRepository = tipoClienteRepository;
    }

    @Override
    public List<TipoCliente> seleccionarTodo() {
        return tipoClienteRepository.findByActivoTrue();
    }

    @Override
    public void actualizarTipoCliente(TipoCliente tipoCliente) {
        tipoClienteRepository.save(tipoCliente);
    }

    @Override
    public void insertarTipoCliente(TipoCliente tipoCliente) {
        tipoClienteRepository.save(tipoCliente);
    }

    @Override
    @Transactional
    public void actualizarEstadoTipoCliente(Long id, int estado) {
        tipoClienteRepository.updateEstado(id, estado);
    }
    @Override
    public List<TipoCliente> seleccionarPorEstados(List<Integer> estados) {
        return tipoClienteRepository.findByEstadoIn(estados);
    }
}
