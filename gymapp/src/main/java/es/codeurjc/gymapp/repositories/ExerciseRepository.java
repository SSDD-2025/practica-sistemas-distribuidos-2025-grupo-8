package es.codeurjc.gymapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.gymapp.model.Exercise;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

}
