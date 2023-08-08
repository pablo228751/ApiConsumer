package ar.com.unont.dato5.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ar.com.unont.dato5.entity.Eventos;


@Repository
public interface IEventosRepository extends JpaRepository<Eventos, Long> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO eventos (evento_id, dni, pago) VALUES (?1, ?2, ?3) ON DUPLICATE KEY UPDATE dni = ?2, pago = ?3", nativeQuery = true)
    void insertOrUpdateEvento(Long eventoId, String dni, String pago);

    List<Eventos> findByFechaEntrada(LocalDate fechaEntrada);
    //List<Eventos> findByFechaEntradaIsNullAndFechaActual(LocalDate fechaActual);
    List<Eventos> findTop200ByFechaEntradaIsNullOrderByEventoIdDesc();
    List<Eventos> findByFechaEntradaAndHoraEntradaIsNull(LocalDate fechaActual);
}