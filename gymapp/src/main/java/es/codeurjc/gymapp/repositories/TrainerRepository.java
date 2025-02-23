package es.codeurjc.gymapp.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.gymapp.model.Trainer;
import org.springframework.stereotype.Repository;
@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
}
