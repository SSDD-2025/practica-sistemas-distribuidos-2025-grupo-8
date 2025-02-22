package es.codeurjc.gymapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.gymapp.model.Material;
public interface MaterialRepository extends JpaRepository<Material, Long> {
    Optional<Material> findByName(String name);
}
