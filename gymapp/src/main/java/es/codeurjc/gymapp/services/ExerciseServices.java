package es.codeurjc.gymapp.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import es.codeurjc.gymapp.DTO.Exercise.ExerciseDTO;
import es.codeurjc.gymapp.DTO.Exercise.ExerciseMapper;
import es.codeurjc.gymapp.DTO.User.UserDTO;
import es.codeurjc.gymapp.DTO.User.UserMapper;
import es.codeurjc.gymapp.model.Exercise;
import es.codeurjc.gymapp.model.Material;
import es.codeurjc.gymapp.model.Routine;
import es.codeurjc.gymapp.model.User;
import es.codeurjc.gymapp.repositories.ExerciseRepository;

@Service
public class ExerciseServices {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private RoutineServices routineServices;

    @Autowired
    private MaterialServices materialServices;

    @Autowired
    private ExerciseMapper mapperExercise;

    @Autowired
    private UserMapper mapperUser;


    public ExerciseServices() {
        // Default constructor
    }

    public long count() {
        return exerciseRepository.count();
    }

    public Optional<ExerciseDTO> findById(Long id) {
        return Optional.of(mapperExercise.toDTO(exerciseRepository.findById(id).get()));
    }

    void save(Exercise exercise) {
        exerciseRepository.save(exercise);
    }

    public void save(String name, String description) {
        Exercise exercise = new Exercise(name, description);
        exerciseRepository.save(exercise);
    }

    public ExerciseDTO save(ExerciseDTO exerciseDTO) {
        Exercise exercise = mapperExercise.toDomain(exerciseDTO);
        exerciseRepository.save(exercise);
        return mapperExercise.toDTO(exercise);
    }

    public ExerciseDTO deleteById(Long id) throws NoSuchElementException{
        Exercise exercise = mapperExercise.toDomain(this.findById(id).get());
        List<Routine> routines = exercise.getRoutine();
        if (!routines.isEmpty()) {
            routineServices.modifyRoutines(mapperExercise.toSimpleDTO(exercise));
        }
    
        if (exercise.getMaterial() != null) {
             materialServices.deleteExerciseFromMaterial(exercise.getMaterial(), exercise.getId());
        }
        exerciseRepository.delete(exercise);
        return mapperExercise.toDTO(exercise);
    }

    public Collection<ExerciseDTO> findAll() {
        return mapperExercise.toDTOs(exerciseRepository.findAll());
    }

    public List<ExerciseDTO> findByMaterialIsNotNull() {
        List<Exercise> exercises = exerciseRepository.findByMaterialIsNotNull();
        return mapperExercise.toDTOs(exercises);
    }

    public List<ExerciseDTO> findAllById(List<Long> id) {
        List<Exercise> exercises = exerciseRepository.findAllById(id);
        return mapperExercise.toDTOs(exercises);
    }

    public void addRoutine(Routine routine, Exercise exercise) {
        exercise.addRoutine(routine);
    }

    public void removeRoutine(Routine routine, Exercise exercise) {
        exercise.getRoutine().remove(routine);
    }

    public Iterable<ExerciseDTO> findExercisesNotAssigned() {
        Iterable<Exercise> exercises = exerciseRepository.findAll();
        List<Exercise> unassignedExercises = new ArrayList<>();
        for (Exercise exercise : exercises) {
            if (exercise.getMaterial() == null) {
                unassignedExercises.add(exercise);
            }
        }
        return mapperExercise.toDTOs(unassignedExercises);
    }

    public void setMaterialAndSave(Exercise exercise, Material material) {
        exercise.setMaterial(material);
        exerciseRepository.save(exercise);
    }

    public void deleteRoutinesFromExercise(UserDTO userDTO) {
        User user = mapperUser.toDomain(userDTO);
        for (Routine routine : user.getRoutines()) {
            for (Exercise exercise : mapperExercise.toDomains(this.findAll())) {
                exercise.getRoutine().remove(routine);
                this.save(exercise);
            }
        }
    }
}
