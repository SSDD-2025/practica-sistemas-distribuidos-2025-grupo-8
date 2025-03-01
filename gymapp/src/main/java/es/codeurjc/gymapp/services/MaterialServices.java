package es.codeurjc.gymapp.services;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import es.codeurjc.gymapp.model.Exercise;
import es.codeurjc.gymapp.model.Material;
import es.codeurjc.gymapp.repositories.MaterialRepository;

@Service
public class MaterialServices {
    
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private ExerciseServices exerciseServices;
    
    public MaterialServices() {
        //materialRepository.save(new Material());
        //materialRepository.save(new Material());
    }

    public Iterable<Material> findAll() {
        return materialRepository.findAll();
    }

    public Optional<Material> findById(Long id) {
        return materialRepository.findById(id);
    }
    
    public void save(Material material, List<Long> exercises) {
        for (Long id: exercises) {
            material.addExercise(exerciseServices.findById(id).get());
        }
        materialRepository.save(material);
    }

    public void save(Material material){
        materialRepository.save(material);
    }

    public void createAndSave(String name, List<Long> exercises) {
        Material material = new Material(name);
        materialRepository.save(material);
        for (Long id: exercises) {
            Exercise exercise = exerciseServices.findById(id).get();
            material.addExercise(exerciseServices.findById(id).get());
            exerciseServices.setMaterialAndSave(exercise, material);
        }
        materialRepository.save(material);
    }

    public void deleteById(Long id) {
        materialRepository.deleteById(id);
    }

    public Optional<Material> findByName(String name) {
        return materialRepository.findByName(name);
    }

    public void safeDelete(Long id) {
        Optional<Material> material = materialRepository.findById(id);
        if (material.isPresent()) {
            for (Exercise exercise: material.get().getExercises()) {
                exercise.setMaterial(null);
                exerciseServices.save(exercise);
            }
            material.get().setExercises(new HashSet<Exercise>());
            materialRepository.save(material.get());
            materialRepository.delete(material.get());
        }
    }

    public void deleteExerciseFromMaterial(Material material, Exercise exercise) {
        Set<Exercise> exercises = material.getExercises();
        exercises.remove(exercise);
        material.setExercises(exercises);
        exercise.setMaterial(null);
        materialRepository.save(material);
        exerciseServices.save(exercise);
    }
}
