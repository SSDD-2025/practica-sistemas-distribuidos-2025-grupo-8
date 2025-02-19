package es.codeurjc.gymapp.model;

import java.util.*;


public class Material extends ModelEntity{
    private Set<Exercise> exercises = new HashSet<Exercise>();

    public Material() {
    }

    public Material(String name) {
        super(name);
    }

    public Set<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(Set<Exercise> exercises) {
        this.exercises = exercises;
    }

    public void addExercise(Exercise exercise) {
        this.exercises.add(exercise);
    }

    public void removeExercise(Exercise exercise) {
        this.exercises.remove(exercise);
    }

}
