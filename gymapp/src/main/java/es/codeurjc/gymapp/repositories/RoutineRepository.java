package es.codeurjc.gymapp.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.gymapp.model.Routine;
public interface RoutineRepository extends JpaRepository<Routine, Long> {

}
