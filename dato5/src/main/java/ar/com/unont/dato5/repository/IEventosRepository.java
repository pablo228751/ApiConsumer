package ar.com.unont.dato5.repository;

import ar.com.unont.dato5.entity.Eventos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEventosRepository extends JpaRepository<Eventos, Long> {
}