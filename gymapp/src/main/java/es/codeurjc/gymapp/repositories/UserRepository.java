package es.codeurjc.gymapp.repositories;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.codeurjc.gymapp.model.User;
<<<<<<< HEAD

=======
import org.springframework.stereotype.Repository;
>>>>>>> 246fc5ccc847b76c5c4fad9adb16827bfcf7eed3
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
}
