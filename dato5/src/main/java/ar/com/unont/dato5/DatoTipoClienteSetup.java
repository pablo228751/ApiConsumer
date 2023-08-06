package ar.com.unont.dato5;


import org.springframework.stereotype.Service;
import ar.com.unont.dato5.entity.TipoCliente;
import ar.com.unont.dato5.service.ITipoClienteService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Service
public class DatoTipoClienteSetup {

    private final ITipoClienteService tipoClienteService;

    
    public DatoTipoClienteSetup(ITipoClienteService tipoClienteService) {
        this.tipoClienteService = tipoClienteService;
    }

    public void lanzar() {
        List<TipoCliente> tipoClientes = tipoClienteService.seleccionarTodo();
        
        if (tipoClientes.isEmpty()) {
            log.warn("No se encontraron registros de TipoCliente.");
            return;
        }
        
        for (TipoCliente tipoCliente : tipoClientes) {
            int estado = tipoCliente.getEstado();
            if (estado == 0) {
                insertEnMolinetes(tipoCliente);
            } else if (estado == 3) {
                deleteEnMolinetes(tipoCliente);
            }
        }
    }

    private void insertEnMolinetes(TipoCliente tipoCliente) {
        // Lógica para insertar en Molinetes cuando el estado es 0.
        // ...
        System.out.println("Insertando en Molinetes: " + tipoCliente.getId());
    }

    private void deleteEnMolinetes(TipoCliente tipoCliente) {
        // Lógica para eliminar en Molinetes cuando el estado es 3.
        // ...
        System.out.println("Eliminando en Molinetes: " + tipoCliente.getId());
    }
}