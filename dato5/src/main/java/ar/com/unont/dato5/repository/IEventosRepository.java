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
    @Query(value = "INSERT INTO eventos (evento_id, dni, fecha_entrada, pago) VALUES (?1, ?2, ?3, ?4) ON DUPLICATE KEY UPDATE dni = ?2, fecha_entrada = ?3, pago = ?4", nativeQuery = true)
    void insertOrUpdateEvento(Long eventoId, String dni,LocalDate fechaEntrada,String pago);

    List<Eventos> findByFechaEntrada(LocalDate fechaEntrada);
    //List<Eventos> findByFechaEntradaIsNullAndFechaActual(LocalDate fechaActual);
    List<Eventos> findTop200ByFechaEntradaIsNullOrderByEventoIdDesc();
    List<Eventos> findByFechaEntradaAndHoraEntradaIsNull(LocalDate fechaActual);
}