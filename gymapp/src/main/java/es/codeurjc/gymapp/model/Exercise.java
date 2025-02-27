package es.codeurjc.gymapp.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Exercise{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    @ManyToOne
    private Material material;
    @ManyToMany
    private List<Routine> routine;

    public Exercise() {
    }

    public Exercise(String name,String description, Material material) {
        this.name = name;
        this.description = description;
        this.material = material;
        this.routine = new ArrayList<>();
    }

    public Exercise(String name, String description){
        this.name = name;
        this.description = description;
        this.material = null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public List<Routine> getRoutine() {
        return routine;
    }

    public void setRoutine(List<Routine> routine){
        this.routine = routine;
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

    public void addRoutine (Routine routine){
        this.routine.add(routine);
    }

    public void removeRoutine(Routine routine){
        this.routine.remove(routine);
    }
}
