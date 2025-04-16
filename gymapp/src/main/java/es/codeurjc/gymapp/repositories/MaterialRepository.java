package es.codeurjc.gymapp.repositories;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.codeurjc.gymapp.model.Material;
@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    Page<Material> findAll(Pageable pageable);
    Optional<Material> findByName(String name);
}
