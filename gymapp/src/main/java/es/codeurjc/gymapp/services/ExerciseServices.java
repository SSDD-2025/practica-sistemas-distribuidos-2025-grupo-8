package es.codeurjc.gymapp.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import es.codeurjc.gymapp.DTO.Exercise.ExerciseMapper;
import es.codeurjc.gymapp.model.Exercise;
import es.codeurjc.gymapp.model.Material;
import es.codeurjc.gymapp.model.Routine;
import es.codeurjc.gymapp.model.User;
import es.codeurjc.gymapp.repositories.ExerciseRepository;

@Service
public class ExerciseServices{

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private RoutineServices routineServices;

    @Autowired
    private MaterialServices materialServices;

    @Autowired
    private ExerciseMapper mapperExercise;


    public ExerciseServices() {
		//exerciseRepository.save(new Exercise());
		//exerciseRepository.save(new Exercise());
	}

    public long count(){
        return exerciseRepository.count();
    }

    public Optional<Exercise> findById(Long id) {
        return exerciseRepository.findById(id);
    }

    public void save(Exercise exercise) {
        exerciseRepository.save(exercise);
    }

    public void save(String name, String description){
        Exercise exercise = new Exercise(name, description);
        exerciseRepository.save(exercise);
    }

    public void deleteById(Long id) {
        Exercise exercise = exerciseRepository.findById(id).get();
        List<Routine> routines = exercise.getRoutine();
        if (!routines.isEmpty()){
            routineServices.modifyRoutines(mapperExercise.toSimpleDTO(exercise));
        }

        if (exercise.getMaterial() != null){
            materialServices.deleteExerciseFromMaterial(exercise.getMaterial(), exercise.getId());
        }
        exerciseRepository.delete(exercise);
    }

    public List<Exercise> findAll() {
        return exerciseRepository.findAll();
    }

    public Exercise findByMaterial(Material material) {
        return exerciseRepository.findByMaterial(material);
    }

    public List<Exercise> findByMaterialIsNotNull(){
        return exerciseRepository.findByMaterialIsNotNull();
    }

    public List<Exercise> findAllById(List<Long> id){
        return exerciseRepository.findAllById(id);
    }

    public void addRoutine(Routine routine, Exercise exercise){
        exercise.addRoutine(routine);
    }

    public void removeRoutine(Routine routine,Exercise exercise){
        exercise.getRoutine().remove(routine);
    }

    public Iterable<Exercise> findExercisesNotAssigned(){
        Iterable<Exercise> ejercicios = exerciseRepository.findAll();
        List<Exercise> ejerciciosNoAsignados = new ArrayList<Exercise>();
        for(Exercise ejercicio : ejercicios){
            if(ejercicio.getMaterial() == null){
                ejerciciosNoAsignados.add(ejercicio);
            }
        }
        return ejerciciosNoAsignados;
    }

    public void setMaterialAndSave(Exercise exercise, Material material){
        exercise.setMaterial(material);
        exerciseRepository.save(exercise);
    }
    public void deleteRoutinesFromExercise(User user) {
		for (Routine routine : user.getRoutines()) {
			for (Exercise exercise : this.findAll()) {
				exercise.getRoutine().remove(routine);
				this.save(exercise);
			}
		}
	}
}
