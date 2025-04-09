package es.codeurjc.gymapp.model;

import jakarta.persistence.*;

import java.nio.file.Path;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Entity(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;
    private String encodedPassword;
    @Lob
    private Blob imageFile;
    @ManyToOne
    private Trainer trainer;
    @OneToMany(mappedBy="userMember")
    private List<Routine> routines;


	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> roles;

    @OneToMany(mappedBy="author")
    private List<Comment> comments;

    public User(){
        
    }

    public User(String name, String encodedPassword, String... roles) {
        this.name = name;
        this.encodedPassword = encodedPassword;
        this.trainer = null;
        this.routines = new ArrayList<>();
        this.roles = List.of(roles);
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
    
    /*public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }*/

    public void addRoutine(Routine routine) {
        this.routines.add(routine);
    }

    public void deleteRoutine(Routine routine){
        this.routines.remove(routine);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    
    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
		this.roles = roles;
	}

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }
}