package es.codeurjc.gymapp.model;

import jakarta.persistence.*;

import java.nio.file.Path;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Entity
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;
    private String password;
    @Lob
    private Blob imageFile;

    @ManyToOne
    private Trainer trainer;
    @OneToMany(mappedBy = "userMember")
    private List<Routine> routines;

    public User(){
        
    }
    // Constructor
    public User(String name, String password) {
        this.password = password;
        this.name = name;
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

    public List<Routine> getRoutines() {
        return routines;
    }

    public void setRoutines(List<Routine> routines) {
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

    public Blob getImageFile() {
        return imageFile;
    }

    public void setImageFile(Blob imageFile) {
        this.imageFile = imageFile;
    }
    
    public void addRoutine(Routine routine) {
        this.routines.add(routine);
    }

    public void deleteRoutine(Routine routine){
        this.routines.remove(routine);
    }
}