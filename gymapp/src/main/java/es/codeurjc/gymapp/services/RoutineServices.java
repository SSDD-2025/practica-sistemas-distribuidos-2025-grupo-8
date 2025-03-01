package es.codeurjc.gymapp.services;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import es.codeurjc.gymapp.model.Exercise;
import es.codeurjc.gymapp.model.Routine;
import es.codeurjc.gymapp.model.User;
import es.codeurjc.gymapp.repositories.RoutineRepository;

@Service
public class RoutineServices {
    
    @Autowired
    private RoutineRepository routineRepository;

    @Autowired
    private ExerciseServices exerciseServices;

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
    
    public List<Routine> findByName(String name){
        return routineRepository.findByName(name);
    }

    public List<Routine> findByUser(User user){
        return routineRepository.findByUserMember(user);
    }

    public void deleteExercises(Routine routine){
        routine.removeExercises();
        routineRepository.save(routine);
    }

    public void addExercises(Routine routine , Set<Exercise> exercises){
        routine.addExercises(exercises);
        routineRepository.save(routine);
    }

    public void deleteUser(Routine routine, User user){
        routine.setUser(null);
        routineRepository.save(routine);
    }

    public void deleteAllRoutines(User user){
        routineRepository.deleteAll(user.getRoutines());
    }

    public void saveExercises(Set<Exercise> exercise, Routine routine){
        for(Exercise ex : exercise){
            exerciseServices.addRoutine(routine, ex);
            exerciseServices.save(ex);
        }
    }

    public void removeExercises(Routine routine){
        for (Exercise exercise : routine.getExercises()) {
            exerciseServices.removeRoutine(routine, exercise);
            exerciseServices.save(exercise);
        }
    }

    public void modifyRoutine(Routine routine, List<Long> exerciseIds){
        this.removeExercises(routine);
        this.deleteExercises(routine);
        this.addExercises(routine, exerciseServices.listToSet(exerciseServices.findAllById(exerciseIds)));
        this.save(routine);
        this.saveExercises(routine.getExercises(), routine);
    }


}
