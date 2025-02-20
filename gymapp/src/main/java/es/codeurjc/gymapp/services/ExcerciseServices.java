package es.codeurjc.gymapp.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import es.codeurjc.gymapp.model.Exercise;
import es.codeurjc.gymapp.repositories.ExerciseRepository;

@Service
public class ExcerciseServices {

    @Autowired
    private ExerciseRepository exerciseRepository;

    public ExcerciseServices() {
		//exerciseRepository.save(new Exercise());
		//exerciseRepository.save(new Exercise());
	}
}
