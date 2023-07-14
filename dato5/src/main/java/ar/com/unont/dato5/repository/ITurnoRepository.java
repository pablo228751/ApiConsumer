package ar.com.unont.dato5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.com.unont.dato5.entity.Turno;

@Repository
public interface ITurnoRepository extends JpaRepository<Turno, Long> {
}