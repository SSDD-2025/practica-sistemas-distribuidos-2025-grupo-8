package es.codeurjc.gymapp.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.gymapp.model.Trainer;
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
}
