package es.codeurjc.gymapp.services;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import es.codeurjc.gymapp.model.Material;
import es.codeurjc.gymapp.repositories.ExerciseRepository;
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

    public void deleteById(Long id) {
        materialRepository.deleteById(id);
    }

    public Optional<Material> findByName(String name) {
        return materialRepository.findByName(name);
    }

}
