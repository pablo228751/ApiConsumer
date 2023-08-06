package ar.com.unont.dato5.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.com.unont.dato5.entity.TipoCliente;

@Repository
public interface ITipoClienteRepository extends JpaRepository<TipoCliente, Long> {
    List<TipoCliente> findByActivoTrue();
}