package es.codeurjc.gymapp.model;

import java.sql.Blob;
import java.util.*;

import jakarta.persistence.*;

@Entity
public class Trainer{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    @Lob
    private Blob imageFile;
    @OneToMany(mappedBy = "trainer")
    private Set<User> users = new HashSet<User>();

    public Trainer() {
    }
    public Trainer(String name) {
        this.name = name;
    }
    public Trainer(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public Trainer(String name, String description, Blob imageFile) {
        this.name = name;
        this.description = description;
        this.imageFile = imageFile;
    }
    public Set<User> getUsers() {
        return users;
    }
    public void setUsers(Set<User> users) {
        this.users = users;
    }
    public void addUser(User user) {
        this.users.add(user);
    }
    public void removeUser(User user) {
        this.users.remove(user);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
