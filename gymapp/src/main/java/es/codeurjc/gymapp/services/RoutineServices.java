package es.codeurjc.gymapp.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import es.codeurjc.gymapp.DTO.Exercise.ExerciseMapper;
import es.codeurjc.gymapp.DTO.Exercise.ExerciseSimpleDTO;
import es.codeurjc.gymapp.DTO.Routine.RoutineDTO;
import es.codeurjc.gymapp.DTO.Routine.RoutineMapper;
import es.codeurjc.gymapp.DTO.Routine.RoutineSimpleDTO;
import es.codeurjc.gymapp.DTO.User.UserDTO;
import es.codeurjc.gymapp.DTO.User.UserMapper;
import es.codeurjc.gymapp.model.Exercise;
import es.codeurjc.gymapp.model.Routine;
import es.codeurjc.gymapp.model.User;
import es.codeurjc.gymapp.repositories.RoutineRepository;

@Service
public class RoutineServices {
    
    @Autowired
    private RoutineRepository routineRepository;

    @Autowired
    private UserServices userServices;
    
    @Autowired
    private ExerciseServices exerciseServices;

    @Autowired
    private RoutineMapper mapperRoutine;

    @Autowired
    private ExerciseMapper mapperExercise;

    @Autowired
    private UserMapper mapperUser;

    public RoutineServices() {
        // routineRepository.save(new Routine());
        // routineRepository.save(new Routine());
    }

    public Optional<RoutineSimpleDTO> findById(Long id) {
        return Optional.of(mapperRoutine.toSimpleDTO(routineRepository.findById(id).get()));
    }

    public Optional<RoutineDTO> findByIdNotSimple(Long id) {
        return Optional.of(mapperRoutine.toDTO(routineRepository.findById(id).get()));
    }

    public void save(RoutineDTO routine) {
        routineRepository.save(mapperRoutine.toDomain(routine));
    }

    public void deleteById(Long id) {
        routineRepository.deleteById(id);
    }
    
    public List<RoutineSimpleDTO> findByName(String name){
        return mapperRoutine.toSimpleDTOs(routineRepository.findByName(name));
    }

    public List<RoutineSimpleDTO> findByUser(UserDTO userDTO){
        User user = mapperUser.toDomain(userDTO);
        return mapperRoutine.toSimpleDTOs(routineRepository.findByUserMember(user));
    }

    public void deleteExercises(RoutineDTO routineDTO){
        Routine routine = mapperRoutine.toDomain(routineDTO);
        routine.removeExercises();
        routineRepository.save(routine);
    }

    public void addExercises(RoutineDTO routineDTO , Set<ExerciseSimpleDTO> exercisesDTO){
        Routine routine = mapperRoutine.toDomain(routineDTO);
        Set<Exercise> exercises = new HashSet<Exercise>(mapperExercise.toDomains(exercisesDTO));
        routine.addExercises(exercises);
        routineRepository.save(routine);
    }

    public void deleteUser(RoutineSimpleDTO routineDTO){
        Routine routine = mapperRoutine.toDomain(routineDTO);
        routine.setUser(null);
        routineRepository.save(routine);
    }

    public void deleteAllRoutines(UserDTO userDTO){
        User user = mapperUser.toDomain(userDTO);
        routineRepository.deleteAll(user.getRoutines());
        user.getRoutines().clear();
        userServices.save(userDTO);
    }

    public void saveExercises(Set<ExerciseSimpleDTO> exerciseDTO, RoutineSimpleDTO routineDTO){
        List<Exercise> exercise = mapperExercise.toDomains(exerciseDTO);
        Routine routine = mapperRoutine.toDomain(routineDTO);
        for(Exercise ex : exercise){
            exerciseServices.addRoutine(routine, ex);
            exerciseServices.save(ex);
        }
    }

    public void removeExercises(RoutineSimpleDTO routineDTO){
        Routine routine = mapperRoutine.toDomain(routineDTO);
        for (Exercise exercise : routine.getExercises()) {
            exerciseServices.removeRoutine(routine, exercise);
            exerciseServices.save(exercise);
        }
    }

    public void modifyRoutine(RoutineDTO routineDTO, List<Long> exerciseIds){
        RoutineSimpleDTO routineSimpleDTO = mapperRoutine.toSimpleDTO(mapperRoutine.toDomain(routineDTO));
        this.removeExercises(routineSimpleDTO);
        this.deleteExercises(routineDTO);
        HashSet<ExerciseSimpleDTO> exercises = new HashSet<ExerciseSimpleDTO>();
        exercises.addAll(mapperExercise.toSimpleDTOs(exerciseServices.findAllById(exerciseIds)));
        this.addExercises(routineDTO, exercises);
        this.save(routineDTO);
        this.saveExercises(routineDTO.exercises(), routineSimpleDTO);
    }

    public void modifyRoutines(ExerciseSimpleDTO exerciseDTO){
        Exercise exercise = mapperExercise.toDomain(exerciseDTO);
        Set<Routine> routinesFromExercise = new HashSet<>(exercise.getRoutine());
        for(Routine routine : routinesFromExercise){
            Set<Exercise> exercisesFromRoutine = routine.getExercises();
            exercisesFromRoutine.remove(exercise);
            routine.setExercises(exercisesFromRoutine);
            routineRepository.save(routine);
        }
        exercise.setRoutine(new ArrayList<>());
    }
}
