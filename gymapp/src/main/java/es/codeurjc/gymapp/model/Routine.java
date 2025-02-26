package es.codeurjc.gymapp.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
public class Routine{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String day;
    @ManyToMany(mappedBy = "routine")
    private Set<Exercise> exercises;
    @ManyToOne
    private User userMember;

    public Routine() {
    }
    public Routine(String name,String description, String day, Set<Exercise> exercises, User user) {
        this(name, description, day, exercises);
        this.userMember = user;
    }
    public Routine(String name,String description, String day, Set<Exercise> exercises) {
        this.name = name;
        this.description = description;
        this.day = day;
        this.exercises = exercises;
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
        this.exercises.clear();
        this.exercises = exercises;
    }

    public User getUser() {
        return userMember;
    }

    public void setUser(User user) {
        this.userMember = user;
    }

    public void addExercise(Exercise exercise) {
        this.exercises.add(exercise);
    }

    public void removeExercise(Exercise exercise) {
        this.exercises.remove(exercise);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
