package es.codeurjc.gymapp.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.codeurjc.gymapp.model.Routine;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {

}
