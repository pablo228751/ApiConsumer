package ar.com.unont.dato5.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.com.unont.dato5.entity.TipoCliente;

@Repository
public interface ITipoClienteRepository extends JpaRepository<TipoCliente, Long> {
    List<TipoCliente> findByActivoTrue();
    
    @Modifying
    @Query("UPDATE TipoCliente t SET t.estado = :estado WHERE t.id = :id")
    void updateEstado(@Param("id") Long id, @Param("estado") int estado);
    List<TipoCliente> findByEstadoIn(List<Integer> estados);
}