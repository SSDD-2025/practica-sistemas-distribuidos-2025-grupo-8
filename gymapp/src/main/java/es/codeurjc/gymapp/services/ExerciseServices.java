package es.codeurjc.gymapp.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import es.codeurjc.gymapp.model.Exercise;
import es.codeurjc.gymapp.model.Material;
import es.codeurjc.gymapp.model.Routine;
import es.codeurjc.gymapp.repositories.ExerciseRepository;

@Service
public class ExerciseServices implements CommandLineRunner{

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Override
    public void run(String... args) throws Exception {
        exerciseRepository.save(new Exercise("Curl de biceps con mancuernas", "De pie o sentado"));
        exerciseRepository.save(new Exercise("Press francés con mancuernas", "Ideal para el tríceps"));
    }

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

    public Exercise findByMaterial(Material material) {
        return exerciseRepository.findByMaterial(material);
    }

    public List<Exercise> findAllById(List<Long> id){
        return exerciseRepository.findAllById(id);
    }

    public Set<Exercise> listToSet(List<Exercise> list){
        Set<Exercise> set = new HashSet<>();
        for(Exercise elem : list){
            set.add(elem);
        }
        return set;
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
}
