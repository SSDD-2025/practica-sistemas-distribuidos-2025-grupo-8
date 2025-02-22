package es.codeurjc.gymapp;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ControllerAdvice;
import es.codeurjc.gymapp.model.UserSession;

@ControllerAdvice
public class GlobalControllerAdvice {

    private UserSession userSession;

    public GlobalControllerAdvice(UserSession userSession) {
        this.userSession = userSession;
    }

    @ModelAttribute("name")
    public String addUserNameToModel() {
        return userSession.isLoggedIn() ? userSession.getName() : "Anónimo";
    }
}
