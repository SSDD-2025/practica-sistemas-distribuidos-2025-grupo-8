package es.codeurjc.gymapp.services;

import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Optional<Trainer> findById(Long id) {
        return trainerRepository.findById(id);
    }

    public void save(Trainer trainer) {
        trainerRepository.save(trainer);
    }

    public void deleteById(Long id) {
        trainerRepository.deleteById(id);
    }

    public Iterable<Trainer> findAll() {
        return trainerRepository.findAll();
    }
}
