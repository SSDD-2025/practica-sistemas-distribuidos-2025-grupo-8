package es.codeurjc.gymapp.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.codeurjc.gymapp.model.Trainer;
<<<<<<< HEAD

=======
import org.springframework.stereotype.Repository;
>>>>>>> 246fc5ccc847b76c5c4fad9adb16827bfcf7eed3
@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
}
