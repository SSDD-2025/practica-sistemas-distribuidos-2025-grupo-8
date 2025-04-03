package es.codeurjc.gymapp.model;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserSession {

    @Autowired
    private AuthenticationManager authenticationManager;

    public String getName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

    public boolean isLoggedIn() {
        return getName() != null || getName() != "";
    }

    public void logout() {
        SecurityContextHolder.clearContext(); 
    }

    public void setName(String name, String password) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(name, password);  // Autenticación con el nombre y la contraseña
		Authentication authenticatedUser = authenticationManager.authenticate(authentication); // Realiza la autenticación
		SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }
}