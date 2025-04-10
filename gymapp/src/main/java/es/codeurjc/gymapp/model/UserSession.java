package es.codeurjc.gymapp.model;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import es.codeurjc.gymapp.repositories.RepositoryUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class UserSession {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RepositoryUserDetailsService userDetailsService;

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
        return getName() != null && !getName().equals("anonymousUser");
    }

    public void logout() {
        SecurityContextHolder.clearContext(); 
    }

    public void setName(String name, String password, HttpServletRequest request) {
        /*Authentication authentication = new UsernamePasswordAuthenticationToken(name, password);  
		Authentication authenticatedUser = authenticationManager.authenticate(authentication); 
		SecurityContextHolder.getContext().setAuthentication(authenticatedUser);*/

        UserDetails userDetails = userDetailsService.loadUserByUsername(name);

        Authentication auth = new UsernamePasswordAuthenticationToken(
            userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);

        request.getSession().setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            SecurityContextHolder.getContext());

    }
}