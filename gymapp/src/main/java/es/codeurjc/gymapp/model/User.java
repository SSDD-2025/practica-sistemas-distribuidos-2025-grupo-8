package es.codeurjc.gymapp.model;

import jakarta.persistence.*;
import java.util.ArrayList;

@Entity
public class User extends ModelEntity {

    private String password;
    private Trainer trainer;
    private ArrayList<Routine> routines;

    // Constructor
    public User(String name, String password) {
        super(name); 
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
}