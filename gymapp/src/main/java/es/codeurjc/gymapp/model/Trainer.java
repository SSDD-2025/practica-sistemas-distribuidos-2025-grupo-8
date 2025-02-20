package es.codeurjc.gymapp.model;

import java.util.*;

import jakarta.persistence.*;

@Entity
public class Trainer extends ModelEntity{
    @OneToMany(mappedBy = "trainer")
    private Set<User> users = new HashSet<User>();

    public Trainer() {
        super();
    }
    public Trainer(String name) {
        super(name);
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
}
