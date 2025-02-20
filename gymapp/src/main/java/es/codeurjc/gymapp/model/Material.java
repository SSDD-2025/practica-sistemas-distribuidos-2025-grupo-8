package es.codeurjc.gymapp.model;

import java.util.*;

import jakarta.persistence.*;

@Entity
public class Material{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "material")
    private Set<Exercise> exercises = new HashSet<Exercise>();

    public Material() {
    }

    public Material(String name) {
        this.name = name;
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
