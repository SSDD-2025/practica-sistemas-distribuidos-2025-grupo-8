package es.codeurjc.gymapp.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.gymapp.model.User;
public interface UserRepository extends JpaRepository<User, Long> {

}
