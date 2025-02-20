package es.codeurjc.gymapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.codeurjc.gymapp.model.User;
import es.codeurjc.gymapp.services.UserServices;

public class UserController {
    
    @Autowired
	private UserServices UserServices;

    @PostMapping("/")
	public String init(Model model) {

		return "index";
	}

    @PostMapping("/facilities")
	public String facilities(Model model) {

		return "facilities";
	}

    @PostMapping("/machinery")
	public String machinery(Model model) {

		return "machinery";
	}

    @PostMapping("/routine")
	public String routines(Model model) {

		return "routine";
	}

    @PostMapping("/trainer")
	public String trainers(Model model) {

		return "trainer";
	}

    

}
