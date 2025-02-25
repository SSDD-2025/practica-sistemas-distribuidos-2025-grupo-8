package es.codeurjc.gymapp;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;

import es.codeurjc.gymapp.model.User;
import es.codeurjc.gymapp.model.UserSession;
import es.codeurjc.gymapp.services.UserServices;

@ControllerAdvice
public class GlobalControllerAdvice {

    private UserSession userSession;

    @Autowired
    private UserServices userServices;

    public GlobalControllerAdvice(UserSession userSession) {
        this.userSession = userSession;
    }

    @ModelAttribute("name")
    public String addUserNameToModel() {
        return userSession.isLoggedIn() ? userSession.getName() : "Anónimo";
    }

    @ModelAttribute("hasImage")
    public boolean addHasImageToModel() {
        if(userSession.isLoggedIn()){
            Optional<User> user = userServices.findByName(userSession.getName());
            return user.get().getImageFile() != null;
        }
        return false;
    }

}
