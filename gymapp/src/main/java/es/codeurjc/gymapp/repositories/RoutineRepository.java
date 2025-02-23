package es.codeurjc.gymapp.repositories;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.codeurjc.gymapp.model.Routine;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
=======
import java.util.List;
>>>>>>> 246fc5ccc847b76c5c4fad9adb16827bfcf7eed3

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.gymapp.model.Exercise;
import es.codeurjc.gymapp.model.Routine;
import org.springframework.stereotype.Repository;
@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    public List<Routine> findByName(String name);
}
