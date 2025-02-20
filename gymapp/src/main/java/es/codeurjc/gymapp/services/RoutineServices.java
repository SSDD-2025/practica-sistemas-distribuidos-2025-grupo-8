package es.codeurjc.gymapp.services;

import org.springframework.stereotype.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import es.codeurjc.gymapp.model.Routine;
import es.codeurjc.gymapp.repositories.RoutineRepository;

@Service
public class RoutineServices {
    
    @Autowired
    private RoutineRepository routineRepository;

    public RoutineServices() {
        // routineRepository.save(new Routine());
        // routineRepository.save(new Routine());
    }

    public Optional<Routine> findById(Long id) {
        return routineRepository.findById(id);
    }

    public void save(Routine routine) {
        routineRepository.save(routine);
    }

    public void deleteById(Long id) {
        routineRepository.deleteById(id);
    }

}
