package es.codeurjc.gymapp.model;

import jakarta.persistence.*;
import java.util.ArrayList;

@Entity
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String password;
    @ManyToOne
    private Trainer trainer;
    @OneToMany(mappedBy = "user")
    private ArrayList<Routine> routines;

    // Constructor
    public User(String name, String password) {
        this.password = password;
        this.trainer = null;
        this.routines = new ArrayList<>();
    }

    // Getters and Setters
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public ArrayList<Routine> getRoutines() {
        return routines;
    }

    public void setRoutines(ArrayList<Routine> routines) {
        this.routines = routines;
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