package ar.com.unont.dato5.service;

import ar.com.unont.dato5.entity.Barrera;
import ar.com.unont.dato5.repository.IBarreraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BarreraServiceImpl implements IBarreraService {

    @Autowired
    private final IBarreraRepository barreraRepository;

    public BarreraServiceImpl(IBarreraRepository barreraRepository) {
        this.barreraRepository = barreraRepository;
    }

    @Override
    public List<Barrera> mostrar() {
        List<Barrera> barreras = barreraRepository.findAll();
        // Eliminar elementos con "activa" menor a 1
        barreras.removeIf(barrera -> barrera.getActiva() < 1);
        return barreras;
    }
}
