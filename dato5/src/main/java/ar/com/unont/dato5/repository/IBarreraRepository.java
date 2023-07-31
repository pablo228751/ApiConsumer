package ar.com.unont.dato5.repository;

import ar.com.unont.dato5.entity.Barrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBarreraRepository extends JpaRepository<Barrera, Long> {
}