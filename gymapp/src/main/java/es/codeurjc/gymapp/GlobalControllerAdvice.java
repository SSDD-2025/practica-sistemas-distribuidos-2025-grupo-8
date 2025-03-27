package es.codeurjc.gymapp;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;

import es.codeurjc.gymapp.DTO.User.UserMapper;
import es.codeurjc.gymapp.model.User;
import es.codeurjc.gymapp.model.UserSession;
import es.codeurjc.gymapp.services.UserServices;

@ControllerAdvice
public class GlobalControllerAdvice {

    private UserSession userSession;

    @Autowired
    private UserServices userServices;
    @Autowired
    private UserMapper userMapper;

    public GlobalControllerAdvice(UserSession userSession) {
        this.userSession = userSession;
    }

    @ModelAttribute("name")
    public String addUserNameToModel() {
        return userSession.isLoggedIn() ? userSession.getName() : "Anónimo";
    }

    @ModelAttribute("hasImage")
    public boolean addHasImageToModel() {
        if (userSession.isLoggedIn()) {
            Optional<User> user = Optional.of(userMapper.toDomain(userServices
                .findByName(userSession.getName()).get()));
    
            if (user.isPresent()) { 
                return user.get().getImageFile() != null; //return true if the user has an image
            } else {
                return false;  
            }
        }
        return false; 
    }

    @ModelAttribute("isAdmin")
    public boolean addIsAdminToModel() {
        if (userSession.isLoggedIn()) {
            Optional<User> user = Optional.of(userMapper.toDomain(userServices
            .findByName(userSession.getName()).get()));
            if (user.isEmpty()) {
                userSession.logout();  
                return false;
            }
            return Boolean.TRUE.equals(user.get().getIsAdmin());
        }
        return false;
    }

}
