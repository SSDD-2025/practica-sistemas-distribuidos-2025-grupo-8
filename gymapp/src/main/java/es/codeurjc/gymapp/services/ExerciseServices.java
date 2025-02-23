package es.codeurjc.gymapp.services;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import es.codeurjc.gymapp.model.Exercise;
import es.codeurjc.gymapp.model.Material;
import es.codeurjc.gymapp.repositories.ExerciseRepository;

@Service
public class ExerciseServices {

    @Autowired
    private ExerciseRepository exerciseRepository;

    public ExerciseServices() {
		//exerciseRepository.save(new Exercise());
		//exerciseRepository.save(new Exercise());
	}

    public Optional<Exercise> findById(Long id) {
        return exerciseRepository.findById(id);
    }

    public void save(Exercise exercise) {
        exerciseRepository.save(exercise);
    }

    public void deleteById(Long id) {
        exerciseRepository.deleteById(id);
    }

    public List<Exercise> findAll() {
        return exerciseRepository.findAll();
    }

    public Material findByMaterial(Material material) {
        return exerciseRepository.findByMaterial(material);
    }
}
