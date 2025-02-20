package es.codeurjc.gymapp.model;

import jakarta.persistence.*;

@Entity
public class Exercise extends ModelEntity{
    
    private String description;
    @ManyToOne
    private Material material;
    @ManyToOne
    private Routine routine;

    public Exercise() {
        super();
    }

    public Exercise(String description, Material material) {
        super();
        this.description = description;
        this.material = material;
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

    public Routine getRoutine() {
        return routine;
    }

    public void setRoutine(Routine routine){
        this.routine = routine;
    }
}
