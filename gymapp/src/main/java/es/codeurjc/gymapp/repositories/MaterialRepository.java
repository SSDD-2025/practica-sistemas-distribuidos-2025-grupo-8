package es.codeurjc.gymapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.gymapp.model.Material;
public interface MaterialRepository extends JpaRepository<Material, Long> {

}
