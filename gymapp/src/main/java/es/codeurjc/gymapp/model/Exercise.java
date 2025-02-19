package es.codeurjc.gymapp.model;

public class Exercise extends ModelEntity{
    
    private String description;
    private Material material;
    private Routine routine;

    public Exercise() {
    }

    public Exercise(String description, Material material) {
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
