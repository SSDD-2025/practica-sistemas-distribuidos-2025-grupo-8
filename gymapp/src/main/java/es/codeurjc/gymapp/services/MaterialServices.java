package es.codeurjc.gymapp.services;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import es.codeurjc.gymapp.DTO.Material.MaterialDTO;
import es.codeurjc.gymapp.DTO.Material.MaterialMapper;
import es.codeurjc.gymapp.model.Exercise;
import es.codeurjc.gymapp.model.Material;
import es.codeurjc.gymapp.repositories.MaterialRepository;

@Service
public class MaterialServices {

    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private ExerciseServices exerciseServices;
    @Autowired
    private MaterialMapper materialMapper;

    public MaterialServices() {
        // Constructor vacío
    }

    public long count() {
        return materialRepository.count();
    }

    public Iterable<MaterialDTO> findAll() {
        return toDTOs(materialRepository.findAll());
    }

    public Optional<MaterialDTO> findById(Long id) {
        return materialRepository.findById(id).map(this::toDTO);
    }

    public Optional<MaterialDTO> findByName(String name) {
        return materialRepository.findByName(name).map(this::toDTO);
    }

    public void save(MaterialDTO materialDTO, List<Long> exercises) {
        Material material = toDomain(materialDTO);
        for (Long id : exercises) {
            material.addExercise(exerciseServices.findById(id).orElseThrow(() -> 
                new IllegalArgumentException("Exercise with ID " + id + " not found")));
        }
        materialRepository.save(material);
    }

    public void save(MaterialDTO materialDTO) {
        Material material = toDomain(materialDTO);
        materialRepository.save(material);
    }

    public void createAndSave(String name, List<Long> exercises) {
        Material material = new Material(name);
        for (Long id : exercises) {
            Exercise exercise = exerciseServices.findById(id).orElseThrow(() -> 
                new IllegalArgumentException("Exercise with ID " + id + " not found"));
            material.addExercise(exercise);
            exerciseServices.setMaterialAndSave(exercise, material);
        }
        materialRepository.save(material);
    }

    public void deleteById(Long id) {
        materialRepository.deleteById(id);
    }

    public void safeDelete(Long id) {
        Optional<Material> material = materialRepository.findById(id);
        if (material.isPresent()) {
            for (Exercise exercise : material.get().getExercises()) {
                exercise.setMaterial(null);
                exerciseServices.save(exercise);
            }
            material.get().setExercises(new HashSet<>());
            materialRepository.save(material.get());
            materialRepository.delete(material.get());
        }
    }

    public void deleteExerciseFromMaterial(Material material, Long exerciseId) {
        Exercise exercise = exerciseServices.findById(exerciseId).orElseThrow(() -> 
            new IllegalArgumentException("Exercise with ID " + exerciseId + " not found"));
        material.getExercises().remove(exercise);
        exercise.setMaterial(null);
        materialRepository.save(material);
        exerciseServices.save(exercise);
    }

    private MaterialDTO toDTO(Material material) {
        return materialMapper.toDTO(material);
    }

    private List<MaterialDTO> toDTOs(Collection<Material> materials) {
        return materialMapper.toDTOs(materials);
    }

    private Material toDomain(MaterialDTO materialDTO) {
        return materialMapper.toDomain(materialDTO);
    }
}
