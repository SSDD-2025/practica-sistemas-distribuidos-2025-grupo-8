package es.codeurjc.gymapp.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.gymapp.model.Exercise;
import es.codeurjc.gymapp.model.Routine;
import org.springframework.stereotype.Repository;
@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    public List<Routine> findByName(String name);
}
