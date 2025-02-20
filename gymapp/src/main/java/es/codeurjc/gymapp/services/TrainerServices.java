package es.codeurjc.gymapp.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import es.codeurjc.gymapp.model.Trainer;
import es.codeurjc.gymapp.repositories.TrainerRepository;

@Service
public class TrainerServices {

    @Autowired
    private TrainerRepository trainerRepository;

    public TrainerServices() {
        // trainerRepository.save(new Trainer());
        // trainerRepository.save(new Trainer());
    }
}
