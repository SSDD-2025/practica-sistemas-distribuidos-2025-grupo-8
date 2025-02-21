package es.codeurjc.gymapp.model;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class UserSession { //separate class to manage the user session

    private String name;

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
    }
}

