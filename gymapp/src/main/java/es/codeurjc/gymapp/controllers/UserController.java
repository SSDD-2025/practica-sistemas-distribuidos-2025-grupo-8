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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.boot.CommandLineRunner;

import es.codeurjc.gymapp.SecurityConfiguration;
import es.codeurjc.gymapp.model.Exercise;
import es.codeurjc.gymapp.model.Material;
import es.codeurjc.gymapp.model.Routine;
import es.codeurjc.gymapp.model.User;
import es.codeurjc.gymapp.model.UserSession;
import es.codeurjc.gymapp.services.CommentService;
import es.codeurjc.gymapp.services.ExerciseServices;
import es.codeurjc.gymapp.services.RoutineServices;
import es.codeurjc.gymapp.services.UserServices;
import jakarta.servlet.http.HttpServletRequest;


@Controller
public class UserController /*implements CommandLineRunner*/ {
    
	@Autowired
	private SecurityConfiguration securityConfiguration;

	@Autowired
	private UserSession userSession;

    @Autowired
	private UserServices userServices;

	@Autowired
	private RoutineServices	routineServices;

	@Autowired
	private ExerciseServices exerciseServices;

	@Autowired
	private CommentService commentServices;

    @RequestMapping("/")
	public String init(Model model) {
		return "index";
	}

	/*@Override
    public void run(String... args) throws Exception {
		if(userServices.count() == 0){
			userServices.save(new User("admin", "admin", true, List.of("ADMIN")));
		}
    }
	*/
	
    @PostMapping("/facilities")
	public String facilities(Model model) {

		return "facilities";
	}

	@RequestMapping("/account")
	public String account(Model model) {
		if(!userSession.isLoggedIn()){
			return "account/account";
		}
		model.addAttribute("name", userSession.getName()); //maybe add more info about the user
		return "account/logged";
	}
	/*
	@PostMapping("/account/login")
	public String sessionInit(Model model, @RequestParam String name, @RequestParam String password) {
		if (name.isEmpty() || password.isEmpty()) {
			model.addAttribute("message", "Nombre de usuario o contraseña no pueden estar vacíos");
			return "error";
		}
		// name is supposed to be unique as a username
		Optional<User> user = userServices.findByName(name); 
		if(!user.isPresent()) {
			model.addAttribute("message", "Usuario no encontrado");
			return "error";
		}
		if(!user.get().getPassword().equals(password)) {
			model.addAttribute("message", "Contraseña incorrecta");
			return "error";
		}
		userSession.setName(name);
		model.addAttribute("message", "Sesión iniciada con éxito");
		return "account/accountMessage";
	}
		*/

	@GetMapping("/account/loginError")
	public String getMethodName(Model model) {
		model.addAttribute("message", "Usuario o contraseña incorrectos");
		return "account/accountMessage";
	}
	
	@RequestMapping("/account/login")
	public String login(Model model) {
		return "redirect:/account"; 
	}
	

	@GetMapping("/register/create")
	public String register(Model model) {
		return "account/register";
	}

	@PostMapping("/account/register")
	public String register(Model model, @RequestParam String name, @RequestParam String password, @RequestParam MultipartFile image,HttpServletRequest request) throws IOException {
		if (name.isEmpty() || password.isEmpty()) {
			model.addAttribute("message", "Nombre de usuario o contraseña no pueden estar vacíos");
			return "error";
		}
		Optional<User> user = userServices.findByName(name);
		if(user.isPresent()){
			model.addAttribute("message", "El usuario ya existe");
			return "error"; //user was already registered
		}
		String encodedPassword = securityConfiguration.passwordEncoder().encode(password);

		User newUser;
		if(image.isEmpty()){		
			newUser = new User(name, encodedPassword,"USER");
		}else{	
			newUser = new User(name, encodedPassword,BlobProxy.generateProxy(image.getInputStream(),image.getSize()),"USER");
		}
    	userServices.save(newUser, image);
		userSession.setName(name, encodedPassword, request);//the auto-login doesn't work
		model.addAttribute("message", "Usuario registrado con éxito");
		return "account/accountMessage";
	}

	@PostMapping("/account/logout")
	public String sessionExit(Model model) {
		/* 
		userSession.logout();
		model.addAttribute("message", "Sesión cerrada");
		*/
		return "account/logout";
	}

	@PostMapping("/account/deleteAccount")
	public String sessionDelete(Model model) {
		Optional<User> optionalUser = userServices.findByName(userSession.getName());
		if (!optionalUser.isPresent()) {
			model.addAttribute("message", "Usuario no encontrado");
			return "error";
		}
		User user = optionalUser.get();

		// Delete routines from exercises
		exerciseServices.deleteRoutinesFromExercise(user);
		// Delete all routines
		routineServices.deleteAllRoutines(user);
		// Delete all comments
		commentServices.deleteAllComments(user);
		// Delete user
		userServices.deleteById(user.getId());
		userSession.logout();
		model.addAttribute("message", "Cuenta eliminada con éxito");
		return "account/accountMessage";
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

	@PostMapping("/admin/users")
	public String showUsers(Model model){
		List<User> users = userServices.findAll();
		model.addAttribute("users", users);
		return "account/show_users";
	}

}
