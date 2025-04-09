package es.codeurjc.gymapp.services;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import es.codeurjc.gymapp.DTO.Exercise.ExerciseMapper;
import es.codeurjc.gymapp.DTO.Material.MaterialDTO;
import es.codeurjc.gymapp.DTO.Material.MaterialMapper;
import es.codeurjc.gymapp.DTO.Material.MaterialSimpleDTO;
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
    @Autowired
    private ExerciseMapper exerciseMapper;

    public MaterialServices() {
        // Constructor vacío
    }

    public long count() {
        return materialRepository.count();
    }

    public Collection<MaterialDTO> findAll() {
        return toDTOs(materialRepository.findAll());
    }

    public MaterialDTO findById(Long id) {
        Optional<Material> op = materialRepository.findById(id);
        if (op.isPresent()) {
            return toDTO(op.get());
        }
        throw new IllegalArgumentException("Material with ID" + id + "not found");
    }

    public Optional<MaterialDTO> findByName(String name) {
        return materialRepository.findByName(name).map(this::toDTO);
    }

    public void save(MaterialDTO materialDTO, List<Long> exercises) throws IllegalArgumentException{
        Material material = toDomain(materialDTO);
        for (Long id : exercises) {
            Exercise exercise =  exerciseMapper.toDomain(exerciseServices.findById(id));
            material.addExercise(exercise);
        }

        materialRepository.save(material);
        }
        

    public MaterialDTO save(MaterialDTO materialDTO) {
        Material material = toDomain(materialDTO);
        materialRepository.save(material);
        return toDTO(material);
    }

    public void save(MaterialSimpleDTO materialDTO) {
        Material material = toDomain(materialDTO);
        materialRepository.save(material);
    }

    public void createAndSave(String name, List<Long> exercises) throws IllegalArgumentException{
        Material material = new Material(name);
        for (Long id : exercises) {
            Exercise exercise = exerciseMapper.toDomain(exerciseServices.findById(id));
            material.addExercise(exercise);
            exerciseServices.setMaterialAndSave(exercise, material);
        }
        materialRepository.save(material);
    }

    public MaterialDTO deleteById(Long id) throws IllegalArgumentException {
        Material material = toDomain(this.findById(id));
        materialRepository.deleteById(id);
        return toDTO(material);
    }

    public void safeDelete(Long id) {
        Material material;
        try {
            material= toDomain(this.findById(id));
            for (Exercise exercise : material.getExercises()) {
                exercise.setMaterial(null);
                exerciseServices.save(exercise);
            }
            material.setExercises(new HashSet<>());
            materialRepository.save(material);
            materialRepository.delete(material);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Material with ID " + id + " not found");
        }
    }

    public void deleteExerciseFromMaterial(Material material, Long exerciseId) throws IllegalArgumentException {
        Exercise exercise = exerciseMapper.toDomain(exerciseServices.findById(exerciseId));
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
    private Material toDomain(MaterialSimpleDTO materialDTO) {
        return materialMapper.toDomain(materialDTO);
    }
}
