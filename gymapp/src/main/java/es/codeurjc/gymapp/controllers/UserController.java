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

		return "index";
	}

    @PostMapping("/facilities")
	public String facilities(Model model) {

		return "facilities";
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
			return "index";
		} else if(user.isPresent() && !user.get().getPassword().equals(password)){
			model.addAttribute("badname", "false");
			return "loginError"; //password was incorrect
		} else {
			model.addAttribute("badname", "true");
			return "loginError"; //user could not be found
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
		return "index"; 		
	}
}
