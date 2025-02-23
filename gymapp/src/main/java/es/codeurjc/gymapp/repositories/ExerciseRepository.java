package es.codeurjc.gymapp.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.gymapp.model.Exercise;
import org.springframework.stereotype.Repository;
@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findAll();
}
