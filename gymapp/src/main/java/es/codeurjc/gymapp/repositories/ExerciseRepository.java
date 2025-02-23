package es.codeurjc.gymapp.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
import org.springframework.stereotype.Repository;

import es.codeurjc.gymapp.model.Exercise;
import es.codeurjc.gymapp.model.Material;

=======
import es.codeurjc.gymapp.model.Exercise;
import org.springframework.stereotype.Repository;
>>>>>>> 246fc5ccc847b76c5c4fad9adb16827bfcf7eed3
@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findAll();
    public Material findByMaterial(Material material);
}
