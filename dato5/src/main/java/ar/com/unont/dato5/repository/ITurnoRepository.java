package ar.com.unont.dato5.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.com.unont.dato5.entity.Turno;

@Repository
public interface ITurnoRepository extends JpaRepository<Turno, Long> {
    @Query("SELECT t FROM Turno t WHERE t.fechaActualizacion = ?1")
    List<Turno> findByFechaActualizacion(LocalDate fechaActualizacion);
}