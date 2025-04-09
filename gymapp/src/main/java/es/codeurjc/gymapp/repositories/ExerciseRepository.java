package es.codeurjc.gymapp.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.gymapp.model.Exercise;
import es.codeurjc.gymapp.model.Material;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findAll();
    public Exercise findByMaterial(Material material);

    public List<Exercise> findByMaterialIsNotNull();
}
