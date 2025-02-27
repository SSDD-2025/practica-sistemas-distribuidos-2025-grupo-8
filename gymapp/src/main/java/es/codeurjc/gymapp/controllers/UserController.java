package es.codeurjc.gymapp.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.boot.CommandLineRunner;

import es.codeurjc.gymapp.model.Exercise;
import es.codeurjc.gymapp.model.Material;
import es.codeurjc.gymapp.model.Routine;
import es.codeurjc.gymapp.model.User;
import es.codeurjc.gymapp.model.UserSession;
import es.codeurjc.gymapp.services.ExerciseServices;
import es.codeurjc.gymapp.services.RoutineServices;
import es.codeurjc.gymapp.services.UserServices;


@Controller
public class UserController implements CommandLineRunner {
    
	@Autowired
	private UserSession userSession;

    @Autowired
	private UserServices userServices;

	@Autowired
	private RoutineServices	routineServices;

	@Autowired
	private ExerciseServices exerciseServices;

    @GetMapping("/")
	public String init(Model model) {

		return "index";
	}

	@Override
    public void run(String... args) throws Exception {
        userServices.save(new User("admin", "admin", true));
    }  
	
    @PostMapping("/facilities")
	public String facilities(Model model) {

		return "facilities";
	}

	@PostMapping("/account")
	public String account(Model model) {
		if(userSession.isLoggedIn()){
			model.addAttribute("name", userSession.getName()); //maybe add more info about the user
			return "account/logged";
		}
		return "account/account"; //user is not logged in
	}

	@PostMapping("/account/login")
	public String sessionInit(Model model, @RequestParam String name, @RequestParam String password) {
		if (name == null || name.isEmpty() || password == null || password.isEmpty()) {
			model.addAttribute("error", "Nombre de usuario o contraseña no pueden estar vacíos");
			return "account/loginError";
		}
		Optional<User> user = userServices.findByName(name); // name is supposed to be unique
		if (user.isPresent() && user.get().getPassword().equals(password)) { // login successful
			userSession.setName(name);
			model.addAttribute("message", "Sesión iniciada con éxito");
			return "account/accountMessage";
		} else if (user.isPresent() && !user.get().getPassword().equals(password)) {
			model.addAttribute("error", "Contraseña incorrecta");
			return "account/loginError"; // password was incorrect
		} else {
			model.addAttribute("error", "Usuario no encontrado");
			return "account/loginError"; // user could not be found
		}
	}

	@GetMapping("/register/create")
	public String register(Model model) {
		return "account/register";
	}

	@PostMapping("/account/register")
	public String register(Model model, @RequestParam String name, @RequestParam String password, @RequestParam MultipartFile image) throws IOException {
		if (name == null || password == null) {
			model.addAttribute("error", "Nombre de usuario o contraseña no pueden estar vacíos");
			return "account/registerError";
		}
		Optional<User> user = userServices.findByName(name);
		if(user.isPresent()){
			model.addAttribute("error", "El usuario ya está en uso");
			return "account/registerError"; //user was already registered
		}
		userSession.setName(name);
		User newUser = new User(name, password, false);
    	userServices.save(newUser, image);
		model.addAttribute("message", "Usuario registrado con éxito");
		return "account/accountMessage";
	}

	@PostMapping("/account/logout")
	public String sessionExit(Model model) {
		userSession.logout();
		model.addAttribute("message", "Sesión cerrada");
		return "account/accountMessage"; 		
	}

	@PostMapping("/account/deleteAccount")
	public String sessionDelete(Model model) {
		Optional<User> optionalUser = userServices.findByName(userSession.getName());
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			
			// Delete routines from exercises
			for (Routine routine : user.getRoutines()) {
				for (Exercise exercise : exerciseServices.findAll()) {
					exercise.getRoutine().remove(routine);
					exerciseServices.save(exercise);
				}
			}

			// Delete all routines
			routineServices.deleteAllRoutines(user);
			
			// Delete user
			userServices.deleteById(user.getId());
			
			model.addAttribute("message", "Cuenta eliminada con éxito");
			return "account/accountMessage";
		} else {
			model.addAttribute("message", "Usuario no encontrado");
			return "account/accountMessage";
		}
	}
	@PostMapping("/account/image")
	public String changeImage(Model model, @RequestParam MultipartFile image) throws IOException {
		Optional<User> user = userServices.findByName(userSession.getName());
		if (image.isEmpty()) {
            user.get().setImageFile(null);
        } else {
            user.get().setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.getSize()));
        }
		userServices.save(user.get(), image);
		model.addAttribute("message", "Imagen cambiada con éxito");
		return "account/accountMessage"; 
	}

	@GetMapping("/user/image")
	public ResponseEntity<Object> downloadImage() throws SQLException {;		
		Optional<User> user = userServices.findByName(userSession.getName());
		if (user.isPresent() && user.get().getImageFile() != null) {			
			Blob image = user.get().getImageFile();
			Resource file = new InputStreamResource(image.getBinaryStream());
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
					.contentLength(image.length()).body(file);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
