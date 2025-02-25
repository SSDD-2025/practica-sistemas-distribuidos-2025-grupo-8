package es.codeurjc.gymapp.model;
import java.sql.Blob;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import jakarta.persistence.Lob;

@Component
@SessionScope
public class UserSession { //separate class to manage the user session

    private String name;
    @Lob
    private Blob imageFile;

    public String getName(){ 
        return name; 
    }
    public void setName(String username){ 
        this.name = username; 
    }

    public boolean isLoggedIn(){ 
        return name != null; 
    }

    public void logout(){ 
        this.name = null; 
        this.imageFile = null;
    }

    public Blob getImageFile(){ 
        return imageFile; 
    }

    public void setImageFile(Blob imageFile){ 
        this.imageFile = imageFile; 
    }
}

