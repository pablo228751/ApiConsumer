package ar.com.unont.dato5.repository;

import ar.com.unont.dato5.entity.Turnero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITurneroRepository extends JpaRepository<Turnero, Long> {
}