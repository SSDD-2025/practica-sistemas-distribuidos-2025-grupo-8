package es.codeurjc.gymapp.model;

import java.util.*;

import jakarta.persistence.*;

@Entity
public class Material extends ModelEntity{
    @OneToMany(mappedBy = "material")
    private Set<Exercise> exercises = new HashSet<Exercise>();

    public Material() {
        super();
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
