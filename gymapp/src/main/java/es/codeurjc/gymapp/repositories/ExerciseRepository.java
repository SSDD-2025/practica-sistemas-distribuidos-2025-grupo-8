package es.codeurjc.gymapp.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.codeurjc.gymapp.model.Exercise;
import es.codeurjc.gymapp.model.Material;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findAll();
    public Material findByMaterial(Material material);
}
