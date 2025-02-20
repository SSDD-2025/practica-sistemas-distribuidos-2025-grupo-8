package es.codeurjc.gymapp.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
public class Routine extends ModelEntity{

    private String description;
    private String day;
    @OneToMany(mappedBy = "routine")
    private Set<Exercise> exercises;
    @ManyToOne
    private User user;

    public Routine() {
        super();
    }
    public Routine(String description, String day, Set<Exercise> exercises, User user) {
        super();
        this.description = description;
        this.day = day;
        this.exercises = new HashSet<>();
        this.user = user;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Set<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(Set<Exercise> exercises) {
        this.exercises = exercises;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addExercise(Exercise exercise) {
        this.exercises.add(exercise);
    }

    public void removeExercise(Exercise exercise) {
        this.exercises.remove(exercise);
    }
}
