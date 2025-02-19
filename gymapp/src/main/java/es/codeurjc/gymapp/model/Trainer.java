package es.codeurjc.gymapp.model;

import java.util.*;

public class Trainer extends ModelEntity{
    private Set<User> users = new HashSet<User>();

    public Trainer() {
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
