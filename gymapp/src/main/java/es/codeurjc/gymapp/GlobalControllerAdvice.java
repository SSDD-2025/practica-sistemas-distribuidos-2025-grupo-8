package es.codeurjc.gymapp;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;

import es.codeurjc.gymapp.DTO.User.UserDTO;
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
    public boolean addHasImageToModel() throws SQLException {
        if (userSession.isLoggedIn()) {
            Optional<UserDTO> user = userServices.findByName(userSession.getName());
            if (user.isPresent()) { 
                return userServices.getUserImage(user.get().id()) != null; //return true if the user has an image
            } else {
                return false;  
            }
        }
        return false; 
    }

    @ModelAttribute("isAdmin")
    public boolean addIsAdminToModel() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {         
            return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return false;
    }

}
