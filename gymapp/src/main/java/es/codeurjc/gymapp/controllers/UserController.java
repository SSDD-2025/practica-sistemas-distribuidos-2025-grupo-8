package es.codeurjc.gymapp.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.codeurjc.gymapp.model.User;
import es.codeurjc.gymapp.model.UserSession;
import es.codeurjc.gymapp.services.UserServices;

@Controller
public class UserController {
    
	@Autowired
	private UserSession userSession;

    @Autowired
	private UserServices userServices;
 
    @GetMapping("/")
	public String init(Model model) {
		if(userSession.isLoggedIn()){ 
			model.addAttribute("name", userSession.getName());
		}else{
			model.addAttribute("name", "Anónimo"); 
		}
		return "index";
	}

    @PostMapping("/facilities")
	public String facilities(Model model) {

		return "facilities";
	}

<<<<<<< HEAD:gymapp/src/main/java/es/codeurjc/gymapp/controllers/UserController.java
    @PostMapping("/routine")
	public String routines(Model model) {

		return "routine";
	}

=======
    @PostMapping("/machinery")
	public String machinery(Model model) {

		return "machinery";
	}
 
>>>>>>> 783765d6035a637ae58234469e856a6c5d384ff0:gymapp/src/main/java/es/codeurjc/gymapp/UserController.java
    @PostMapping("/trainer")
	public String trainers(Model model) {

		return "trainer";
	}

	@PostMapping("/account")
	public String account(Model model) {
		if(userSession.isLoggedIn()){
			model.addAttribute("name", userSession.getName()); //maybe add more info about the user
			return "logged";
		}
		return "account"; //user is not logged in
	}

	@PostMapping("/account/login")
	public String sessionInit(Model model, @RequestParam String name, @RequestParam String password) {
		Optional<User> user = userServices.findByName(name); //name is supposed to be unique
		if(user.isPresent() && user.get().getPassword().equals(password)){ //login successful
			userSession.setName(name);
			model.addAttribute("name", name);
			return "index";
		} else{
			model.addAttribute("badname", "false");
			return "loginError"; //user could not be found or password was incorrect
		}
	}
	@PostMapping("/account/register")
	public String register(Model model, @RequestParam String name, @RequestParam String password) {
		Optional<User> user = userServices.findByName(name);
		if(user.isPresent()){
			return "registerError"; //user was already registered
		}
		userSession.setName(name);
		userServices.save(new User(name, password));
		return "registerSuccess";
	}
	@PostMapping("/account/logout")
	public String sessionExit(Model model) {
		userSession.logout();
		model.addAttribute("name", "Anónimo");
		return "index"; 
	}

    

}
