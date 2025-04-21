package es.codeurjc.gymapp.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import es.codeurjc.gymapp.DTO.Exercise.ExerciseDTO;
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

    public RoutineDTO save(RoutineDTO dto) {
        Routine routine = mapperRoutine.toDomain(dto);
        Routine saved = routineRepository.save(routine);
        return mapperRoutine.toDTO(saved);
    }

    void save(Routine routine) {
        routineRepository.save(routine);
    }

    void save(Collection<Routine> routines) {
        for (Routine routine : routines) {
            routineRepository.save(routine);
        }
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
    
    void deleteExercises(Routine routine){
        routine.removeExercises();
        routineRepository.save(routine);
    }

    public void deleteExercises(RoutineDTO routineDTO){
        deleteExercises(mapperRoutine.toDomain(routineDTO));
    }


    public void addExercises(RoutineDTO routineDTO , Set<ExerciseDTO> exercisesDTO){
        Routine routine = mapperRoutine.toDomain(routineDTO);
        Set<Exercise> exercises = new HashSet<Exercise>(mapperExercise.toDomains(exercisesDTO));
        routine.addExercises(exercises);
        routineRepository.save(routine);
    }

    public void deleteUser(RoutineSimpleDTO routineDTO){
        deleteUser(mapperRoutine.toDomain(routineDTO));
    }

    public void deleteUser(RoutineDTO routineDTO){
        deleteUser(mapperRoutine.toDomain(routineDTO));
    }

    void deleteUser(Routine routine){
        routine.setUser(null);
        routineRepository.save(routine);
    }

    public void deleteAllRoutines(UserDTO userDTO){
        User user = mapperUser.toDomain(userDTO);
        routineRepository.deleteAll(user.getRoutines());
        user.getRoutines().clear();
        userServices.save(user);
    }

    public void saveExercises(Set<ExerciseSimpleDTO> exerciseDTO, RoutineSimpleDTO routineDTO){
        Set<Exercise> exercises = new HashSet<>(mapperExercise.toDomainsSimple(exerciseDTO));
        saveExercises(exercises, mapperRoutine.toDomain(routineDTO));
    }

    public void saveExercises(Set<ExerciseDTO> exerciseDTO, RoutineDTO routineDTO){
        Set<Exercise> exercises = new HashSet<>(mapperExercise.toDomains(exerciseDTO));
        saveExercises(exercises, mapperRoutine.toDomain(routineDTO));
    }

    void saveExercises(Set<Exercise> exercise, Routine routine){
        for(Exercise ex : exercise){
            exerciseServices.addRoutine(routine, ex);
            exerciseServices.save(ex);
        }
    }

    void removeExercises(Routine routine){
        for (Exercise exercise : routine.getExercises()) {
            exerciseServices.removeRoutine(routine, exercise);
            exerciseServices.save(exercise);
        }
    }

    public void removeExercises(RoutineDTO routine){
        removeExercises(mapperRoutine.toDomain(routine));
    }

    public void modifyRoutine(RoutineDTO routineDTO, List<Long> exerciseIds){
        Routine routine = mapperRoutine.toDomain(routineDTO);
        List<Exercise> exercises = mapperExercise.toDomains(exerciseServices.findAllById(exerciseIds));
        Set<Exercise> exercisesSet = new HashSet<>(exercises);
        //Erase the old conections 
        removeExercises(routine);
        deleteExercises(routine);
        //Conections broken now
        //Add the new connections
        routine.setExercises(exercisesSet);
        routineRepository.save(routine);
        saveExercises(exercisesSet, routine);
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
