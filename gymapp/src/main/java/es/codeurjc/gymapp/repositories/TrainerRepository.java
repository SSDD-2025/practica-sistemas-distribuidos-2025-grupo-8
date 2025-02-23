package es.codeurjc.gymapp.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.codeurjc.gymapp.model.Trainer;
@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
}
