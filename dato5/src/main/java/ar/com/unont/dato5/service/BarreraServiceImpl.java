package ar.com.unont.dato5.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.unont.dato5.Dato5Application;
import ar.com.unont.dato5.entity.Barrera;
import ar.com.unont.dato5.repository.IBarreraRepository;

@Service
public class BarreraServiceImpl implements IBarreraService {

    private static Logger LOG = LoggerFactory.getLogger(Dato5Application.class);

    @Autowired
    private IBarreraRepository barreraRepository;

    @Override
    public Barrera guardar(Barrera barrera) {
        return barreraRepository.save(barrera);
    }

    @Override
    public Optional<Barrera> buscarPorId(int id) {
        return barreraRepository.findById(id);
    }

    @Override
    public Barrera actualizar(int id, Barrera barrera) {
        return barrera;
    }

    @Override
    public void mostrar() {
        List<Barrera> barreras = barreraRepository.findAll();
        for (Barrera barrera : barreras) {
            LOG.info("IP: "+barrera.getIp()+" Descripcion: "+barrera.getDescripcion()+ " Puerto: "+barrera.getPuerto());
            }
    }
}
