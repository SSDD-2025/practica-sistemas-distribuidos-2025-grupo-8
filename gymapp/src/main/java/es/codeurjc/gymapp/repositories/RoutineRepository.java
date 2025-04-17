package es.codeurjc.gymapp.repositories;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.gymapp.model.Routine;
import es.codeurjc.gymapp.model.User;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
    public List<Routine> findByName(String name);
    public List<Routine> findByUserMember(User user);
    public Page<Routine> findByUserMember(User user, Pageable pageable);
}
